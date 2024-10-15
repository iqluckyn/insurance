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
import { IFarmer } from 'app/shared/model/farmer.model';
import { getEntities as getFarmers } from 'app/entities/farmer/farmer.reducer';
import { IFarm } from 'app/shared/model/farm.model';
import { getEntity, updateEntity, createEntity, reset } from './farm.reducer';

export const FarmUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const cropTypes = useAppSelector(state => state.cropType.entities);
  const farmers = useAppSelector(state => state.farmer.entities);
  const farmEntity = useAppSelector(state => state.farm.entity);
  const loading = useAppSelector(state => state.farm.loading);
  const updating = useAppSelector(state => state.farm.updating);
  const updateSuccess = useAppSelector(state => state.farm.updateSuccess);

  const handleClose = () => {
    navigate('/farm' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getCropTypes({}));
    dispatch(getFarmers({}));
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
    if (values.latitude !== undefined && typeof values.latitude !== 'number') {
      values.latitude = Number(values.latitude);
    }
    if (values.longitude !== undefined && typeof values.longitude !== 'number') {
      values.longitude = Number(values.longitude);
    }

    const entity = {
      ...farmEntity,
      ...values,
      cropType: cropTypes.find(it => it.id.toString() === values.cropType?.toString()),
      farmer: farmers.find(it => it.id.toString() === values.farmer?.toString()),
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
          ...farmEntity,
          cropType: farmEntity?.cropType?.id,
          farmer: farmEntity?.farmer?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="policyadminApp.farm.home.createOrEditLabel" data-cy="FarmCreateUpdateHeading">
            Create or edit a Farm
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="farm-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField
                label="Latitude"
                id="farm-latitude"
                name="latitude"
                data-cy="latitude"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                  validate: v => isNumber(v) || 'This field should be a number.',
                }}
              />
              <ValidatedField
                label="Longitude"
                id="farm-longitude"
                name="longitude"
                data-cy="longitude"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                  validate: v => isNumber(v) || 'This field should be a number.',
                }}
              />
              <ValidatedField
                label="Cell Identifier"
                id="farm-cellIdentifier"
                name="cellIdentifier"
                data-cy="cellIdentifier"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                }}
              />
              <ValidatedField id="farm-cropType" name="cropType" data-cy="cropType" label="Crop Type" type="select">
                <option value="" key="0" />
                {cropTypes
                  ? cropTypes.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.cropName}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField id="farm-farmer" name="farmer" data-cy="farmer" label="Farmer" type="select">
                <option value="" key="0" />
                {farmers
                  ? farmers.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/farm" replace color="info">
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

export default FarmUpdate;
