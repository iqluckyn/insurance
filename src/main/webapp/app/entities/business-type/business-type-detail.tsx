import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './business-type.reducer';

export const BusinessTypeDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const businessTypeEntity = useAppSelector(state => state.businessType.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="businessTypeDetailsHeading">Business Type</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{businessTypeEntity.id}</dd>
          <dt>
            <span id="name">Name</span>
          </dt>
          <dd>{businessTypeEntity.name}</dd>
          <dt>
            <span id="description">Description</span>
          </dt>
          <dd>{businessTypeEntity.description}</dd>
        </dl>
        <Button tag={Link} to="/business-type" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/business-type/${businessTypeEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default BusinessTypeDetail;
