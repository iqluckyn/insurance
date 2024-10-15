import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './p-component.reducer';

export const PComponentDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const pComponentEntity = useAppSelector(state => state.pComponent.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="pComponentDetailsHeading">P Component</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{pComponentEntity.id}</dd>
          <dt>
            <span id="name">Name</span>
          </dt>
          <dd>{pComponentEntity.name}</dd>
          <dt>
            <span id="description">Description</span>
          </dt>
          <dd>{pComponentEntity.description}</dd>
        </dl>
        <Button tag={Link} to="/p-component" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/p-component/${pComponentEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default PComponentDetail;
