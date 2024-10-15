import React, { useState, useEffect, useCallback } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText, Card, CardBody, CardHeader } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import {
  AzureMapsProvider,
  AzureMap,
  IAzureMapOptions,
  AzureMapDataSourceProvider,
  AzureMapLayerProvider,
  AzureMapFeature,
} from 'react-azure-maps';
import { LineChart, Line, XAxis, YAxis, CartesianGrid, Tooltip, Legend, ResponsiveContainer } from 'recharts';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getSeasons } from 'app/entities/season/season.reducer';
import { getEntities as getFarmers } from 'app/entities/farmer/farmer.reducer';
import { getEntities as getProducts } from 'app/entities/product/product.reducer';
import { getEntities as getBusinesses } from 'app/entities/business/business.reducer';
import { getEntities as getQuotationStatuses } from 'app/entities/quotation-status/quotation-status.reducer';
import { getEntity, updateEntity, createEntity, reset } from './quotation.reducer';
import { IFarm } from 'app/shared/model/farm.model';
import { createEntity as createFarm } from 'app/entities/farm/farm.reducer';

const azureMapOptions: IAzureMapOptions = {
  authOptions: {
    authType: 'subscriptionKey', // Use 'subscriptionKey' as a string
    subscriptionKey: 'CoKRRu4KI9IJcEGSH8yo4vZ3zjYqSiLC2RcZHkXvOx2Odz03gZO0JQQJ99AGAC5RqLJpiCioAAAgAZMP77aG',
  },
  center: [28.0473, -26.2041],
  zoom: 8,
};

export const QuotationDetail = () => {
  const dispatch = useAppDispatch();
  const navigate = useNavigate();
  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const [farmData, setFarmData] = useState<IFarm>({ latitude: null, longitude: null, cellIdentifier: '' });
  const [smiChartData, setSmiChartData] = useState<any[]>([]);

  const seasons = useAppSelector(state => state.season.entities);
  const farmers = useAppSelector(state => state.farmer.entities);
  const products = useAppSelector(state => state.product.entities);
  const businesses = useAppSelector(state => state.business.entities);
  const quotationStatuses = useAppSelector(state => state.quotationStatus.entities);
  const quotationEntity = useAppSelector(state => state.quotation.entity);
  const loading = useAppSelector(state => state.quotation.loading);
  const updating = useAppSelector(state => state.quotation.updating);
  const updateSuccess = useAppSelector(state => state.quotation.updateSuccess);

  const handleClose = () => {
    navigate('/quotation' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getSeasons({}));
    dispatch(getFarmers({}));
    dispatch(getProducts({}));
    dispatch(getBusinesses({}));
    dispatch(getQuotationStatuses({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const onMapClick = useCallback((e: any) => {
    const clickedLocation: [number, number] = [e.position[0], e.position[1]];
    setFarmData({
      latitude: e.position[1],
      longitude: e.position[0],
      cellIdentifier: `Cell-${Math.floor(e.position[1] * 100)}-${Math.floor(e.position[0] * 100)}`,
    });
  }, []);

  useEffect(() => {
    const data = Array.from({ length: 12 }, (_, i) => ({
      month: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'][i],
      smi: Math.random() * 100,
    }));
    setSmiChartData(data);
  }, []);

  const saveEntity = values => {
    const farmEntity: IFarm = {
      latitude: farmData.latitude,
      longitude: farmData.longitude,
      cellIdentifier: farmData.cellIdentifier,
      farmer: farmers.find(it => it.id.toString() === values.farmer?.toString()),
    };

    values.createdAt = convertDateTimeToServer(values.createdAt);
    values.updatedAt = convertDateTimeToServer(values.updatedAt);

    const entity = {
      ...quotationEntity,
      ...values,
      season: seasons.find(it => it.id.toString() === values.season?.toString()),
      farmer: farmers.find(it => it.id.toString() === values.farmer?.toString()),
      product: products.find(it => it.id.toString() === values.product?.toString()),
      business: businesses.find(it => it.id.toString() === values.business?.toString()),
      quotationStatus: quotationStatuses.find(it => it.id.toString() === values.quotationStatus?.toString()),
    };

    dispatch(createFarm(farmEntity))
      .then((farmResult: any) => {
        const farm = farmResult.payload.data as IFarm;
        entity.farm = farm;

        if (isNew) {
          dispatch(createEntity(entity));
        } else {
          dispatch(updateEntity(entity));
        }
      })
      .catch(error => {
        console.error('Error creating farm:', error);
      });
  };

  const defaultValues = () =>
    isNew
      ? {
          createdAt: displayDefaultDateTime(),
          updatedAt: displayDefaultDateTime(),
        }
      : {
          ...quotationEntity,
          createdAt: convertDateTimeFromServer(quotationEntity.createdAt),
          updatedAt: convertDateTimeFromServer(quotationEntity.updatedAt),
          season: quotationEntity?.season?.id,
          farmer: quotationEntity?.farmer?.id,
          product: quotationEntity?.product?.id,
          business: quotationEntity?.business?.id,
          quotationStatus: quotationEntity?.quotationStatus?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="12">
          <Card>
            <CardHeader>
              <h2 id="policyadminApp.quotation.home.createOrEditLabel" data-cy="QuotationCreateUpdateHeading">
                Create or edit a Quotation
              </h2>
            </CardHeader>
            <CardBody>
              <Row>
                <Col md="9">
                  <Card>
                    <CardHeader>Map</CardHeader>
                    <CardBody>
                      <div style={{ height: '400px' }}>
                        <AzureMapsProvider>
                          <AzureMap options={azureMapOptions}>
                            <AzureMapDataSourceProvider id="dataSource">
                              <AzureMapLayerProvider id="layer" options={{}} type="SymbolLayer" events={{ click: onMapClick }} />
                              {farmData.latitude && farmData.longitude && (
                                <AzureMapFeature
                                  type="Point"
                                  coordinate={[farmData.longitude, farmData.latitude]}
                                  properties={{ title: 'Pinned Location' }}
                                />
                              )}
                            </AzureMapDataSourceProvider>
                          </AzureMap>
                        </AzureMapsProvider>
                      </div>
                    </CardBody>
                  </Card>
                </Col>
                <Col md="3">
                  <Card>
                    <CardHeader>Farm Details</CardHeader>
                    <CardBody>
                      <p>
                        <strong>Latitude:</strong> {farmData.latitude}
                      </p>
                      <p>
                        <strong>Longitude:</strong> {farmData.longitude}
                      </p>
                      <p>
                        <strong>Cell Identifier:</strong> {farmData.cellIdentifier}
                      </p>
                    </CardBody>
                  </Card>
                </Col>
              </Row>
              {loading ? (
                <p>Loading...</p>
              ) : (
                <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
                  {!isNew ? (
                    <ValidatedField name="id" required readOnly id="quotation-id" label="ID" validate={{ required: true }} />
                  ) : null}
                  <ValidatedField
                    label="Start Of Risk Period"
                    id="quotation-startOfRiskPeriod"
                    name="startOfRiskPeriod"
                    data-cy="startOfRiskPeriod"
                    type="date"
                    validate={{
                      required: { value: true, message: 'This field is required.' },
                    }}
                  />
                  <ValidatedField
                    label="Length Of Risk Period"
                    id="quotation-lengthOfRiskPeriod"
                    name="lengthOfRiskPeriod"
                    data-cy="lengthOfRiskPeriod"
                    type="range"
                    min="30"
                    max="60"
                    step="10"
                    validate={{
                      required: { value: true, message: 'This field is required.' },
                      validate: v => isNumber(v) || 'This field should be a number.',
                    }}
                  />
                  <ValidatedField
                    label="Depth"
                    id="quotation-depth"
                    name="depth"
                    data-cy="depth"
                    type="range"
                    min="20"
                    max="40"
                    step="20"
                    validate={{
                      required: { value: true, message: 'This field is required.' },
                      validate: v => isNumber(v) || 'This field should be a number.',
                    }}
                  />
                  <ValidatedField
                    label="Claims Frequency"
                    id="quotation-claimsFrequency"
                    name="claimsFrequency"
                    data-cy="claimsFrequency"
                    type="range"
                    min="5"
                    max="12"
                    step="1"
                    validate={{
                      required: { value: true, message: 'This field is required.' },
                      validate: v => isNumber(v) || 'This field should be a number.',
                    }}
                  />
                  <ValidatedField
                    label="Insured Value"
                    id="quotation-insuredValue"
                    name="insuredValue"
                    data-cy="insuredValue"
                    type="range"
                    min="10000"
                    max="25000"
                    step="5000"
                    validate={{
                      required: { value: true, message: 'This field is required.' },
                      validate: v => isNumber(v) || 'This field should be a number.',
                    }}
                  />
                  <ValidatedField
                    label="Best Premium"
                    id="quotation-bestPremium"
                    name="bestPremium"
                    data-cy="bestPremium"
                    type="text"
                    validate={{
                      required: { value: true, message: 'This field is required.' },
                      validate: v => isNumber(v) || 'This field should be a number.',
                    }}
                  />
                  <ValidatedField
                    label="Insured Rate"
                    id="quotation-insuredRate"
                    name="insuredRate"
                    data-cy="insuredRate"
                    type="range"
                    min="2"
                    max="10"
                    step="1"
                    validate={{
                      required: { value: true, message: 'This field is required.' },
                      validate: v => isNumber(v) || 'This field should be a number.',
                    }}
                  />
                  <ValidatedField
                    label="Insured Premium"
                    id="quotation-insuredPremium"
                    name="insuredPremium"
                    data-cy="insuredPremium"
                    type="text"
                    validate={{
                      required: { value: true, message: 'This field is required.' },
                      validate: v => isNumber(v) || 'This field should be a number.',
                    }}
                  />
                  <ValidatedField
                    label="Created At"
                    id="quotation-createdAt"
                    name="createdAt"
                    data-cy="createdAt"
                    type="datetime-local"
                    placeholder="YYYY-MM-DD HH:mm"
                  />
                  <ValidatedField
                    label="Updated At"
                    id="quotation-updatedAt"
                    name="updatedAt"
                    data-cy="updatedAt"
                    type="datetime-local"
                    placeholder="YYYY-MM-DD HH:mm"
                  />
                  <ValidatedField id="quotation-season" name="season" data-cy="season" label="Season" type="select">
                    <option value="" key="0" />
                    {seasons
                      ? seasons.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.seasonName}
                          </option>
                        ))
                      : null}
                  </ValidatedField>
                  <ValidatedField id="quotation-farmer" name="farmer" data-cy="farmer" label="Farmer" type="select">
                    <option value="" key="0" />
                    {farmers
                      ? farmers.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.firstname}
                          </option>
                        ))
                      : null}
                  </ValidatedField>
                  <ValidatedField id="quotation-product" name="product" data-cy="product" label="Product" type="select">
                    <option value="" key="0" />
                    {products
                      ? products.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.productName}
                          </option>
                        ))
                      : null}
                  </ValidatedField>
                  <ValidatedField id="quotation-business" name="business" data-cy="business" label="Business" type="select">
                    <option value="" key="0" />
                    {businesses
                      ? businesses.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.businessName}
                          </option>
                        ))
                      : null}
                  </ValidatedField>
                  <ValidatedField
                    id="quotation-quotationStatus"
                    name="quotationStatus"
                    data-cy="quotationStatus"
                    label="Quotation Status"
                    type="select"
                  >
                    <option value="" key="0" />
                    {quotationStatuses
                      ? quotationStatuses.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.statusCode}
                          </option>
                        ))
                      : null}
                  </ValidatedField>
                  <div style={{ height: '300px', marginBottom: '20px' }}>
                    <ResponsiveContainer width="100%" height="100%">
                      <LineChart data={smiChartData}>
                        <CartesianGrid strokeDasharray="3 3" />
                        <XAxis dataKey="month" />
                        <YAxis />
                        <Tooltip />
                        <Legend />
                        <Line type="monotone" dataKey="smi" stroke="#8884d8" activeDot={{ r: 8 }} />
                      </LineChart>
                    </ResponsiveContainer>
                  </div>
                  <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/quotation" replace color="success">
                    <FontAwesomeIcon icon="arrow-left" />
                    &nbsp;
                    <span className="d-none d-md-inline">Back</span>
                  </Button>
                  &nbsp;
                  <Button color="warning" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                    <FontAwesomeIcon icon="save" />
                    &nbsp; Save
                  </Button>
                </ValidatedForm>
              )}
            </CardBody>
          </Card>
        </Col>
      </Row>
    </div>
  );
};

export default QuotationDetail;
