import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './policy-component.reducer';

export const PolicyComponentDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const policyComponentEntity = useAppSelector(state => state.policyComponent.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="policyComponentDetailsHeading">Policy Component</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{policyComponentEntity.id}</dd>
          <dt>
            <span id="componentValue">Component Value</span>
          </dt>
          <dd>{policyComponentEntity.componentValue}</dd>
          <dt>
            <span id="createdAt">Created At</span>
          </dt>
          <dd>
            {policyComponentEntity.createdAt ? (
              <TextFormat value={policyComponentEntity.createdAt} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="updatedAt">Updated At</span>
          </dt>
          <dd>
            {policyComponentEntity.updatedAt ? (
              <TextFormat value={policyComponentEntity.updatedAt} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>Component</dt>
          <dd>{policyComponentEntity.component ? policyComponentEntity.component.name : ''}</dd>
          <dt>Policies</dt>
          <dd>
            {policyComponentEntity.policies
              ? policyComponentEntity.policies.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {policyComponentEntity.policies && i === policyComponentEntity.policies.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button tag={Link} to="/policy-component" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/policy-component/${policyComponentEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default PolicyComponentDetail;
