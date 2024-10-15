import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IPComponent } from 'app/shared/model/p-component.model';
import { getEntities as getPComponents } from 'app/entities/p-component/p-component.reducer';
import { IInsuredPolicy } from 'app/shared/model/insured-policy.model';
import { getEntities as getInsuredPolicies } from 'app/entities/insured-policy/insured-policy.reducer';
import { IPolicyComponent } from 'app/shared/model/policy-component.model';
import { getEntity, updateEntity, createEntity, reset } from './policy-component.reducer';

export const PolicyComponentUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const pComponents = useAppSelector(state => state.pComponent.entities);
  const insuredPolicies = useAppSelector(state => state.insuredPolicy.entities);
  const policyComponentEntity = useAppSelector(state => state.policyComponent.entity);
  const loading = useAppSelector(state => state.policyComponent.loading);
  const updating = useAppSelector(state => state.policyComponent.updating);
  const updateSuccess = useAppSelector(state => state.policyComponent.updateSuccess);

  const handleClose = () => {
    navigate('/policy-component' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getPComponents({}));
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
    if (values.componentValue !== undefined && typeof values.componentValue !== 'number') {
      values.componentValue = Number(values.componentValue);
    }
    values.createdAt = convertDateTimeToServer(values.createdAt);
    values.updatedAt = convertDateTimeToServer(values.updatedAt);

    const entity = {
      ...policyComponentEntity,
      ...values,
      component: pComponents.find(it => it.id.toString() === values.component?.toString()),
      policies: mapIdList(values.policies),
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
          ...policyComponentEntity,
          createdAt: convertDateTimeFromServer(policyComponentEntity.createdAt),
          updatedAt: convertDateTimeFromServer(policyComponentEntity.updatedAt),
          component: policyComponentEntity?.component?.id,
          policies: policyComponentEntity?.policies?.map(e => e.id.toString()),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="policyadminApp.policyComponent.home.createOrEditLabel" data-cy="PolicyComponentCreateUpdateHeading">
            Create or edit a Policy Component
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
                <ValidatedField name="id" required readOnly id="policy-component-id" label="ID" validate={{ required: true }} />
              ) : null}
              <ValidatedField
                label="Component Value"
                id="policy-component-componentValue"
                name="componentValue"
                data-cy="componentValue"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                  validate: v => isNumber(v) || 'This field should be a number.',
                }}
              />
              <ValidatedField
                label="Created At"
                id="policy-component-createdAt"
                name="createdAt"
                data-cy="createdAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label="Updated At"
                id="policy-component-updatedAt"
                name="updatedAt"
                data-cy="updatedAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField id="policy-component-component" name="component" data-cy="component" label="Component" type="select">
                <option value="" key="0" />
                {pComponents
                  ? pComponents.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.name}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField label="Policies" id="policy-component-policies" data-cy="policies" type="select" multiple name="policies">
                <option value="" key="0" />
                {insuredPolicies
                  ? insuredPolicies.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/policy-component" replace color="info">
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

export default PolicyComponentUpdate;
