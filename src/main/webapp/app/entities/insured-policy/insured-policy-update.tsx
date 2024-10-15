import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IQuotation } from 'app/shared/model/quotation.model';
import { getEntities as getQuotations } from 'app/entities/quotation/quotation.reducer';
import { IFarmer } from 'app/shared/model/farmer.model';
import { getEntities as getFarmers } from 'app/entities/farmer/farmer.reducer';
import { IFarm } from 'app/shared/model/farm.model';
import { getEntities as getFarms } from 'app/entities/farm/farm.reducer';
import { IPolicyComponent } from 'app/shared/model/policy-component.model';
import { getEntities as getPolicyComponents } from 'app/entities/policy-component/policy-component.reducer';
import { IInsuredPolicy } from 'app/shared/model/insured-policy.model';
import { PolicyStatus } from 'app/shared/model/enumerations/policy-status.model';
import { getEntity, updateEntity, createEntity, reset } from './insured-policy.reducer';

export const InsuredPolicyUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const quotations = useAppSelector(state => state.quotation.entities);
  const farmers = useAppSelector(state => state.farmer.entities);
  const farms = useAppSelector(state => state.farm.entities);
  const policyComponents = useAppSelector(state => state.policyComponent.entities);
  const insuredPolicyEntity = useAppSelector(state => state.insuredPolicy.entity);
  const loading = useAppSelector(state => state.insuredPolicy.loading);
  const updating = useAppSelector(state => state.insuredPolicy.updating);
  const updateSuccess = useAppSelector(state => state.insuredPolicy.updateSuccess);
  const policyStatusValues = Object.keys(PolicyStatus);

  const handleClose = () => {
    navigate('/insured-policy' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getQuotations({}));
    dispatch(getFarmers({}));
    dispatch(getFarms({}));
    dispatch(getPolicyComponents({}));
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
    values.startDate = convertDateTimeToServer(values.startDate);
    values.endDate = convertDateTimeToServer(values.endDate);
    if (values.premiumAmount !== undefined && typeof values.premiumAmount !== 'number') {
      values.premiumAmount = Number(values.premiumAmount);
    }
    if (values.coverageAmount !== undefined && typeof values.coverageAmount !== 'number') {
      values.coverageAmount = Number(values.coverageAmount);
    }

    const entity = {
      ...insuredPolicyEntity,
      ...values,
      policyQuotation: quotations.find(it => it.id.toString() === values.policyQuotation?.toString()),
      insuredFarmer: farmers.find(it => it.id.toString() === values.insuredFarmer?.toString()),
      farm: farms.find(it => it.id.toString() === values.farm?.toString()),
      components: mapIdList(values.components),
      quotation: quotations.find(it => it.id.toString() === values.quotation?.toString()),
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
          startDate: displayDefaultDateTime(),
          endDate: displayDefaultDateTime(),
        }
      : {
          status: 'PENDING',
          ...insuredPolicyEntity,
          startDate: convertDateTimeFromServer(insuredPolicyEntity.startDate),
          endDate: convertDateTimeFromServer(insuredPolicyEntity.endDate),
          policyQuotation: insuredPolicyEntity?.policyQuotation?.id,
          insuredFarmer: insuredPolicyEntity?.insuredFarmer?.id,
          farm: insuredPolicyEntity?.farm?.id,
          components: insuredPolicyEntity?.components?.map(e => e.id.toString()),
          quotation: insuredPolicyEntity?.quotation?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="policyadminApp.insuredPolicy.home.createOrEditLabel" data-cy="InsuredPolicyCreateUpdateHeading">
            Create or edit a Insured Policy
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
                <ValidatedField name="id" required readOnly id="insured-policy-id" label="ID" validate={{ required: true }} />
              ) : null}
              <ValidatedField
                label="Policy Number"
                id="insured-policy-policyNumber"
                name="policyNumber"
                data-cy="policyNumber"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                }}
              />
              <ValidatedField
                label="Start Date"
                id="insured-policy-startDate"
                name="startDate"
                data-cy="startDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                }}
              />
              <ValidatedField
                label="End Date"
                id="insured-policy-endDate"
                name="endDate"
                data-cy="endDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                }}
              />
              <ValidatedField
                label="Premium Amount"
                id="insured-policy-premiumAmount"
                name="premiumAmount"
                data-cy="premiumAmount"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                  validate: v => isNumber(v) || 'This field should be a number.',
                }}
              />
              <ValidatedField
                label="Coverage Amount"
                id="insured-policy-coverageAmount"
                name="coverageAmount"
                data-cy="coverageAmount"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                  validate: v => isNumber(v) || 'This field should be a number.',
                }}
              />
              <ValidatedField label="Status" id="insured-policy-status" name="status" data-cy="status" type="select">
                {policyStatusValues.map(policyStatus => (
                  <option value={policyStatus} key={policyStatus}>
                    {policyStatus}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                id="insured-policy-policyQuotation"
                name="policyQuotation"
                data-cy="policyQuotation"
                label="Policy Quotation"
                type="select"
              >
                <option value="" key="0" />
                {quotations
                  ? quotations.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="insured-policy-insuredFarmer"
                name="insuredFarmer"
                data-cy="insuredFarmer"
                label="Insured Farmer"
                type="select"
              >
                <option value="" key="0" />
                {farmers
                  ? farmers.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField id="insured-policy-farm" name="farm" data-cy="farm" label="Farm" type="select">
                <option value="" key="0" />
                {farms
                  ? farms.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                label="Components"
                id="insured-policy-components"
                data-cy="components"
                type="select"
                multiple
                name="components"
              >
                <option value="" key="0" />
                {policyComponents
                  ? policyComponents.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField id="insured-policy-quotation" name="quotation" data-cy="quotation" label="Quotation" type="select">
                <option value="" key="0" />
                {quotations
                  ? quotations.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/insured-policy" replace color="info">
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

export default InsuredPolicyUpdate;
