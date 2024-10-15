import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IInsuredPolicy } from 'app/shared/model/insured-policy.model';
import { getEntities as getInsuredPolicies } from 'app/entities/insured-policy/insured-policy.reducer';
import { IPolicyClaim } from 'app/shared/model/policy-claim.model';
import { ClaimStatus } from 'app/shared/model/enumerations/claim-status.model';
import { getEntity, updateEntity, createEntity, reset } from './policy-claim.reducer';

export const PolicyClaimUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const insuredPolicies = useAppSelector(state => state.insuredPolicy.entities);
  const policyClaimEntity = useAppSelector(state => state.policyClaim.entity);
  const loading = useAppSelector(state => state.policyClaim.loading);
  const updating = useAppSelector(state => state.policyClaim.updating);
  const updateSuccess = useAppSelector(state => state.policyClaim.updateSuccess);
  const claimStatusValues = Object.keys(ClaimStatus);

  const handleClose = () => {
    navigate('/policy-claim' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getInsuredPolicies({}));
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
    values.claimDate = convertDateTimeToServer(values.claimDate);
    if (values.amountClaimed !== undefined && typeof values.amountClaimed !== 'number') {
      values.amountClaimed = Number(values.amountClaimed);
    }

    const entity = {
      ...policyClaimEntity,
      ...values,
      policy: insuredPolicies.find(it => it.id.toString() === values.policy?.toString()),
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
          claimDate: displayDefaultDateTime(),
        }
      : {
          status: 'OPEN',
          ...policyClaimEntity,
          claimDate: convertDateTimeFromServer(policyClaimEntity.claimDate),
          policy: policyClaimEntity?.policy?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="policyadminApp.policyClaim.home.createOrEditLabel" data-cy="PolicyClaimCreateUpdateHeading">
            Create or edit a Policy Claim
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="policy-claim-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField
                label="Claim Number"
                id="policy-claim-claimNumber"
                name="claimNumber"
                data-cy="claimNumber"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                }}
              />
              <ValidatedField
                label="Claim Date"
                id="policy-claim-claimDate"
                name="claimDate"
                data-cy="claimDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                }}
              />
              <ValidatedField
                label="Amount Claimed"
                id="policy-claim-amountClaimed"
                name="amountClaimed"
                data-cy="amountClaimed"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                  validate: v => isNumber(v) || 'This field should be a number.',
                }}
              />
              <ValidatedField label="Status" id="policy-claim-status" name="status" data-cy="status" type="select">
                {claimStatusValues.map(claimStatus => (
                  <option value={claimStatus} key={claimStatus}>
                    {claimStatus}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField id="policy-claim-policy" name="policy" data-cy="policy" label="Policy" type="select">
                <option value="" key="0" />
                {insuredPolicies
                  ? insuredPolicies.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.policyNumber}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/policy-claim" replace color="info">
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

export default PolicyClaimUpdate;
