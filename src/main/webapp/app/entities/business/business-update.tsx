import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IBusinessType } from 'app/shared/model/business-type.model';
import { getEntities as getBusinessTypes } from 'app/entities/business-type/business-type.reducer';
import { IBusiness } from 'app/shared/model/business.model';
import { getEntity, updateEntity, createEntity, reset } from './business.reducer';

export const BusinessUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const businessTypes = useAppSelector(state => state.businessType.entities);
  const businessEntity = useAppSelector(state => state.business.entity);
  const loading = useAppSelector(state => state.business.loading);
  const updating = useAppSelector(state => state.business.updating);
  const updateSuccess = useAppSelector(state => state.business.updateSuccess);

  const handleClose = () => {
    navigate('/business' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getBusinessTypes({}));
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
    values.createdAt = convertDateTimeToServer(values.createdAt);
    values.updatedAt = convertDateTimeToServer(values.updatedAt);

    const entity = {
      ...businessEntity,
      ...values,
      businessType: businessTypes.find(it => it.id.toString() === values.businessType?.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          createdAt: displayDefaultDateTime(),
          updatedAt: displayDefaultDateTime(),
        }
      : {
          ...businessEntity,
          createdAt: convertDateTimeFromServer(businessEntity.createdAt),
          updatedAt: convertDateTimeFromServer(businessEntity.updatedAt),
          businessType: businessEntity?.businessType?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="policyadminApp.business.home.createOrEditLabel" data-cy="BusinessCreateUpdateHeading">
            Create or edit a Business
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="business-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField
                label="Registered Name"
                id="business-registeredName"
                name="registeredName"
                data-cy="registeredName"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                }}
              />
              <ValidatedField
                label="Organisation Name"
                id="business-organisationName"
                name="organisationName"
                data-cy="organisationName"
                type="text"
              />
              <ValidatedField label="Vat Number" id="business-vatNumber" name="vatNumber" data-cy="vatNumber" type="text" />
              <ValidatedField
                label="Created At"
                id="business-createdAt"
                name="createdAt"
                data-cy="createdAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label="Updated At"
                id="business-updatedAt"
                name="updatedAt"
                data-cy="updatedAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField id="business-businessType" name="businessType" data-cy="businessType" label="Business Type" type="select">
                <option value="" key="0" />
                {businessTypes
                  ? businessTypes.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.name}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/business" replace color="info">
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

export default BusinessUpdate;
