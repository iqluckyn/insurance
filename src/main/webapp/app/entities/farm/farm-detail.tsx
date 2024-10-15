import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './farm.reducer';

export const FarmDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const farmEntity = useAppSelector(state => state.farm.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="farmDetailsHeading">Farm</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{farmEntity.id}</dd>
          <dt>
            <span id="latitude">Latitude</span>
          </dt>
          <dd>{farmEntity.latitude}</dd>
          <dt>
            <span id="longitude">Longitude</span>
          </dt>
          <dd>{farmEntity.longitude}</dd>
          <dt>
            <span id="cellIdentifier">Cell Identifier</span>
          </dt>
          <dd>{farmEntity.cellIdentifier}</dd>
          <dt>Crop Type</dt>
          <dd>{farmEntity.cropType ? farmEntity.cropType.cropName : ''}</dd>
          <dt>Farmer</dt>
          <dd>{farmEntity.farmer ? farmEntity.farmer.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/farm" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/farm/${farmEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default FarmDetail;
