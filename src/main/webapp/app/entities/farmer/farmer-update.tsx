import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, Card, CardBody, CardHeader, Form } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IUser } from 'app/shared/model/user.model';
import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { IBusiness } from 'app/shared/model/business.model';
import { getEntities as getBusinesses } from 'app/entities/business/business.reducer';
import { IFarmer } from 'app/shared/model/farmer.model';
import { getEntity, updateEntity, createEntity, reset } from './farmer.reducer';

export const FarmerUpdate = () => {
  const dispatch = useAppDispatch();
  const navigate = useNavigate();
  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const users = useAppSelector(state => state.userManagement.users);
  const businesses = useAppSelector(state => state.business.entities);
  const farmerEntity = useAppSelector(state => state.farmer.entity);
  const loading = useAppSelector(state => state.farmer.loading);
  const updating = useAppSelector(state => state.farmer.updating);
  const updateSuccess = useAppSelector(state => state.farmer.updateSuccess);

  const handleClose = () => {
    navigate('/farmer' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getUsers({}));
    dispatch(getBusinesses({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    values.registrationDate = convertDateTimeToServer(values.registrationDate);

    const entity = {
      ...farmerEntity,
      ...values,
      user: users.find(it => it.id.toString() === values.user?.toString()),
      business: businesses.find(it => it.id.toString() === values.business?.toString()),
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
          registrationDate: displayDefaultDateTime(),
        }
      : {
          ...farmerEntity,
          registrationDate: convertDateTimeFromServer(farmerEntity.registrationDate),
          user: farmerEntity?.user?.id,
          business: farmerEntity?.business?.id,
        };

  return (
    <div className="container-fluid">
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="policyadminApp.farmer.home.createOrEditLabel" data-cy="FarmerCreateUpdateHeading">
            {isNew ? 'Create a Farmer' : 'Edit Farmer'}
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="farmer-id" label="ID" validate={{ required: true }} /> : null}

              <Card className="mb-3">
                <CardHeader>Personal Information</CardHeader>
                <CardBody>
                  <Row>
                    <Col md={6}>
                      <ValidatedField
                        label="Firstname"
                        id="farmer-firstname"
                        name="firstname"
                        data-cy="firstname"
                        type="text"
                        validate={{
                          required: { value: true, message: 'This field is required.' },
                        }}
                      />
                    </Col>
                    <Col md={6}>
                      <ValidatedField
                        label="Lastname"
                        id="farmer-lastname"
                        name="lastname"
                        data-cy="lastname"
                        type="text"
                        validate={{
                          required: { value: true, message: 'This field is required.' },
                        }}
                      />
                    </Col>
                  </Row>
                  <Row>
                    <Col md={6}>
                      <ValidatedField
                        label="Email"
                        id="farmer-email"
                        name="email"
                        data-cy="email"
                        type="text"
                        validate={{
                          required: { value: true, message: 'This field is required.' },
                        }}
                      />
                    </Col>
                    <Col md={6}>
                      <ValidatedField label="Phone" id="farmer-phone" name="phone" data-cy="phone" type="text" />
                    </Col>
                  </Row>
                </CardBody>
              </Card>

              <Card className="mb-3">
                <CardHeader>Business Information</CardHeader>
                <CardBody>
                  <Row>
                    <Col md={6}>
                      <ValidatedField
                        label="Position"
                        id="farmer-position"
                        name="position"
                        data-cy="position"
                        type="text"
                        validate={{
                          required: { value: true, message: 'This field is required.' },
                        }}
                      />
                    </Col>
                    <Col md={6}>
                      <ValidatedField id="farmer-business" name="business" data-cy="business" label="Business" type="select">
                        <option value="" key="0" />
                        {businesses
                          ? businesses.map(otherEntity => (
                              <option value={otherEntity.id} key={otherEntity.id}>
                                {otherEntity.registeredName}
                              </option>
                            ))
                          : null}
                      </ValidatedField>
                    </Col>
                  </Row>
                </CardBody>
              </Card>

              <Card className="mb-3">
                <CardHeader>Address Information</CardHeader>
                <CardBody>
                  <Row>
                    <Col md={12}>
                      <ValidatedField label="Address" id="farmer-address" name="address" data-cy="address" type="text" />
                    </Col>
                  </Row>
                  <Row>
                    <Col md={6}>
                      <ValidatedField label="City" id="farmer-city" name="city" data-cy="city" type="text" />
                    </Col>
                    <Col md={6}>
                      <ValidatedField label="Province" id="farmer-province" name="province" data-cy="province" type="text" />
                    </Col>
                  </Row>
                  <Row>
                    <Col md={6}>
                      <ValidatedField label="Country" id="farmer-country" name="country" data-cy="country" type="text" />
                    </Col>
                    <Col md={6}>
                      <ValidatedField label="Postal Code" id="farmer-postalCode" name="postalCode" data-cy="postalCode" type="text" />
                    </Col>
                  </Row>
                </CardBody>
              </Card>

              <Card className="mb-3">
                <CardHeader>Account Information</CardHeader>
                <CardBody>
                  <Row>
                    <Col md={6}>
                      <ValidatedField id="farmer-user" name="user" data-cy="user" label="User" type="select">
                        <option value="" key="0" />
                        {users
                          ? users.map(otherEntity => (
                              <option value={otherEntity.id} key={otherEntity.id}>
                                {otherEntity.login}
                              </option>
                            ))
                          : null}
                      </ValidatedField>
                    </Col>
                    <Col md={6}>
                      <ValidatedField
                        label="Registration Date"
                        id="farmer-registrationDate"
                        name="registrationDate"
                        data-cy="registrationDate"
                        type="datetime-local"
                        placeholder="YYYY-MM-DD HH:mm"
                      />
                    </Col>
                  </Row>
                </CardBody>
              </Card>

              <div className="d-flex justify-content-end">
                <Button
                  tag={Link}
                  id="cancel-save"
                  data-cy="entityCreateCancelButton"
                  to="/farmer"
                  replace
                  color="secondary"
                  className="me-2"
                >
                  <FontAwesomeIcon icon="arrow-left" />
                  &nbsp;
                  <span className="d-none d-md-inline">Back</span>
                </Button>
                <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                  <FontAwesomeIcon icon="save" />
                  &nbsp; Save
                </Button>
              </div>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default FarmerUpdate;
