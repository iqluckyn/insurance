import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './quotation-status.reducer';

export const QuotationStatusDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const quotationStatusEntity = useAppSelector(state => state.quotationStatus.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="quotationStatusDetailsHeading">Quotation Status</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{quotationStatusEntity.id}</dd>
          <dt>
            <span id="statusName">Status Name</span>
          </dt>
          <dd>{quotationStatusEntity.statusName}</dd>
          <dt>
            <span id="statusCode">Status Code</span>
          </dt>
          <dd>{quotationStatusEntity.statusCode}</dd>
          <dt>
            <span id="description">Description</span>
          </dt>
          <dd>{quotationStatusEntity.description}</dd>
          <dt>
            <span id="isActive">Is Active</span>
          </dt>
          <dd>{quotationStatusEntity.isActive ? 'true' : 'false'}</dd>
        </dl>
        <Button tag={Link} to="/quotation-status" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/quotation-status/${quotationStatusEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default QuotationStatusDetail;
