import axios, { AxiosError } from 'axios';
import { createAsyncThunk, createSlice, isFulfilled, isPending } from '@reduxjs/toolkit';
import { cleanEntity } from 'app/shared/util/entity-utils';
import { IQueryParams, EntityState, serializeAxiosError } from 'app/shared/reducers/reducer.utils';
import { IFarmer, defaultValue } from 'app/shared/model/farmer.model';

const initialState: EntityState<IFarmer> = {
  loading: false,
  errorMessage: null,
  entities: [],
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

const apiUrl = 'api/farmers';

// Axios instance with timeout
const axiosInstance = axios.create({
  timeout: 10000, // 10 seconds timeout
});

// Actions

export const getEntities = createAsyncThunk(
  'farmer/fetch_entity_list',
  async ({ page, size, sort }: IQueryParams, { signal }) => {
    const requestUrl = `${apiUrl}?${sort ? `page=${page}&size=${size}&sort=${sort}&` : ''}`;
    return axiosInstance.get<IFarmer[]>(requestUrl, { signal });
  },
  { serializeError: serializeAxiosError },
);

export const getEntity = createAsyncThunk(
  'farmer/fetch_entity',
  async (id: string | number, { signal }) => {
    const requestUrl = `${apiUrl}/${id}`;
    return axiosInstance.get<IFarmer>(requestUrl, { signal });
  },
  { serializeError: serializeAxiosError },
);

export const createEntity = createAsyncThunk(
  'farmer/create_entity',
  async (entity: IFarmer, { dispatch }) => {
    const result = await axiosInstance.post<IFarmer>(apiUrl, cleanEntity(entity));
    dispatch(getEntities({ page: 0, size: 20, sort: 'id,asc' }));
    return result;
  },
  { serializeError: serializeAxiosError },
);

export const updateEntity = createAsyncThunk(
  'farmer/update_entity',
  async (entity: IFarmer, { dispatch }) => {
    const result = await axiosInstance.put<IFarmer>(`${apiUrl}/${entity.id}`, cleanEntity(entity));
    dispatch(getEntities({ page: 0, size: 20, sort: 'id,asc' }));
    return result;
  },
  { serializeError: serializeAxiosError },
);

export const partialUpdateEntity = createAsyncThunk(
  'farmer/partial_update_entity',
  async (entity: IFarmer, { dispatch }) => {
    const result = await axiosInstance.patch<IFarmer>(`${apiUrl}/${entity.id}`, cleanEntity(entity));
    dispatch(getEntities({ page: 0, size: 20, sort: 'id,asc' }));
    return result;
  },
  { serializeError: serializeAxiosError },
);

export const deleteEntity = createAsyncThunk(
  'farmer/delete_entity',
  async (id: string | number, { dispatch }) => {
    const requestUrl = `${apiUrl}/${id}`;
    const result = await axiosInstance.delete<IFarmer>(requestUrl);
    dispatch(getEntities({ page: 0, size: 20, sort: 'id,asc' }));
    return result;
  },
  { serializeError: serializeAxiosError },
);

// Slice

export const FarmerSlice = createSlice({
  name: 'farmer',
  initialState,
  reducers: {
    reset: state => initialState,
  },
  extraReducers(builder) {
    builder
      .addCase(getEntity.fulfilled, (state, action) => {
        state.loading = false;
        state.entity = action.payload.data;
      })
      .addCase(deleteEntity.fulfilled, state => {
        state.updating = false;
        state.updateSuccess = true;
        state.entity = {};
      })
      .addMatcher(isFulfilled(getEntities), (state, action) => {
        const { data, headers } = action.payload;
        return {
          ...state,
          loading: false,
          entities: data,
          totalItems: parseInt(headers['x-total-count'], 10),
        };
      })
      .addMatcher(isFulfilled(createEntity, updateEntity, partialUpdateEntity), (state, action) => {
        state.updating = false;
        state.loading = false;
        state.updateSuccess = true;
        state.entity = action.payload.data;
      })
      .addMatcher(isPending(getEntities, getEntity), state => {
        state.errorMessage = null;
        state.updateSuccess = false;
        state.loading = true;
      })
      .addMatcher(isPending(createEntity, updateEntity, partialUpdateEntity, deleteEntity), state => {
        state.errorMessage = null;
        state.updateSuccess = false;
        state.updating = true;
      })
      .addMatcher(
        action => action.type.endsWith('/rejected'),
        (state, action) => {
          state.loading = false;
          state.updating = false;
        },
      );
  },
});

export const { reset } = FarmerSlice.actions;

export default FarmerSlice.reducer;
