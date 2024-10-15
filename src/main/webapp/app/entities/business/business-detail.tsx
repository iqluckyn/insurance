import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './business.reducer';

export const BusinessDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const businessEntity = useAppSelector(state => state.business.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="businessDetailsHeading">Business</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{businessEntity.id}</dd>
          <dt>
            <span id="registeredName">Registered Name</span>
          </dt>
          <dd>{businessEntity.registeredName}</dd>
          <dt>
            <span id="organisationName">Organisation Name</span>
          </dt>
          <dd>{businessEntity.organisationName}</dd>
          <dt>
            <span id="vatNumber">Vat Number</span>
          </dt>
          <dd>{businessEntity.vatNumber}</dd>
          <dt>
            <span id="createdAt">Created At</span>
          </dt>
          <dd>{businessEntity.createdAt ? <TextFormat value={businessEntity.createdAt} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="updatedAt">Updated At</span>
          </dt>
          <dd>{businessEntity.updatedAt ? <TextFormat value={businessEntity.updatedAt} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>Business Type</dt>
          <dd>{businessEntity.businessType ? businessEntity.businessType.name : ''}</dd>
        </dl>
        <Button tag={Link} to="/business" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/business/${businessEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default BusinessDetail;
