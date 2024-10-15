import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './crop-type.reducer';

export const CropTypeDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const cropTypeEntity = useAppSelector(state => state.cropType.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="cropTypeDetailsHeading">Crop Type</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{cropTypeEntity.id}</dd>
          <dt>
            <span id="cropName">Crop Name</span>
          </dt>
          <dd>{cropTypeEntity.cropName}</dd>
        </dl>
        <Button tag={Link} to="/crop-type" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/crop-type/${cropTypeEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default CropTypeDetail;
