import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ICropType } from 'app/shared/model/crop-type.model';
import { getEntities as getCropTypes } from 'app/entities/crop-type/crop-type.reducer';
import { ISeason } from 'app/shared/model/season.model';
import { getEntity, updateEntity, createEntity, reset } from './season.reducer';

export const SeasonUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const cropTypes = useAppSelector(state => state.cropType.entities);
  const seasonEntity = useAppSelector(state => state.season.entity);
  const loading = useAppSelector(state => state.season.loading);
  const updating = useAppSelector(state => state.season.updating);
  const updateSuccess = useAppSelector(state => state.season.updateSuccess);

  const handleClose = () => {
    navigate('/season' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getCropTypes({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  // eslint-disable-next-line complexity
  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }

    const entity = {
      ...seasonEntity,
      ...values,
      cropType: cropTypes.find(it => it.id.toString() === values.cropType?.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...seasonEntity,
          cropType: seasonEntity?.cropType?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="policyadminApp.season.home.createOrEditLabel" data-cy="SeasonCreateUpdateHeading">
            Create or edit a Season
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="season-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField
                label="Season Name"
                id="season-seasonName"
                name="seasonName"
                data-cy="seasonName"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                }}
              />
              <ValidatedField
                label="Start Date"
                id="season-startDate"
                name="startDate"
                data-cy="startDate"
                type="date"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                }}
              />
              <ValidatedField
                label="End Date"
                id="season-endDate"
                name="endDate"
                data-cy="endDate"
                type="date"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                }}
              />
              <ValidatedField label="Is Active" id="season-isActive" name="isActive" data-cy="isActive" check type="checkbox" />
              <ValidatedField id="season-cropType" name="cropType" data-cy="cropType" label="Crop Type" type="select">
                <option value="" key="0" />
                {cropTypes
                  ? cropTypes.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/season" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">Back</span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp; Save
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default SeasonUpdate;
