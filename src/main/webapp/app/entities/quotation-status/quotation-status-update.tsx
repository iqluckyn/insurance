import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IQuotationStatus } from 'app/shared/model/quotation-status.model';
import { getEntity, updateEntity, createEntity, reset } from './quotation-status.reducer';

export const QuotationStatusUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const quotationStatusEntity = useAppSelector(state => state.quotationStatus.entity);
  const loading = useAppSelector(state => state.quotationStatus.loading);
  const updating = useAppSelector(state => state.quotationStatus.updating);
  const updateSuccess = useAppSelector(state => state.quotationStatus.updateSuccess);

  const handleClose = () => {
    navigate('/quotation-status' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }
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
      ...quotationStatusEntity,
      ...values,
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
          ...quotationStatusEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="policyadminApp.quotationStatus.home.createOrEditLabel" data-cy="QuotationStatusCreateUpdateHeading">
            Create or edit a Quotation Status
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField name="id" required readOnly id="quotation-status-id" label="ID" validate={{ required: true }} />
              ) : null}
              <ValidatedField
                label="Status Name"
                id="quotation-status-statusName"
                name="statusName"
                data-cy="statusName"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                }}
              />
              <ValidatedField
                label="Status Code"
                id="quotation-status-statusCode"
                name="statusCode"
                data-cy="statusCode"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                }}
              />
              <ValidatedField label="Description" id="quotation-status-description" name="description" data-cy="description" type="text" />
              <ValidatedField label="Is Active" id="quotation-status-isActive" name="isActive" data-cy="isActive" check type="checkbox" />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/quotation-status" replace color="info">
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

export default QuotationStatusUpdate;
