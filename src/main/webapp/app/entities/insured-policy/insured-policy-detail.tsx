import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './insured-policy.reducer';

export const InsuredPolicyDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const insuredPolicyEntity = useAppSelector(state => state.insuredPolicy.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="insuredPolicyDetailsHeading">Insured Policy</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{insuredPolicyEntity.id}</dd>
          <dt>
            <span id="policyNumber">Policy Number</span>
          </dt>
          <dd>{insuredPolicyEntity.policyNumber}</dd>
          <dt>
            <span id="startDate">Start Date</span>
          </dt>
          <dd>
            {insuredPolicyEntity.startDate ? (
              <TextFormat value={insuredPolicyEntity.startDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="endDate">End Date</span>
          </dt>
          <dd>
            {insuredPolicyEntity.endDate ? <TextFormat value={insuredPolicyEntity.endDate} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="premiumAmount">Premium Amount</span>
          </dt>
          <dd>{insuredPolicyEntity.premiumAmount}</dd>
          <dt>
            <span id="coverageAmount">Coverage Amount</span>
          </dt>
          <dd>{insuredPolicyEntity.coverageAmount}</dd>
          <dt>
            <span id="status">Status</span>
          </dt>
          <dd>{insuredPolicyEntity.status}</dd>
          <dt>Policy Quotation</dt>
          <dd>{insuredPolicyEntity.policyQuotation ? insuredPolicyEntity.policyQuotation.id : ''}</dd>
          <dt>Insured Farmer</dt>
          <dd>{insuredPolicyEntity.insuredFarmer ? insuredPolicyEntity.insuredFarmer.id : ''}</dd>
          <dt>Farm</dt>
          <dd>{insuredPolicyEntity.farm ? insuredPolicyEntity.farm.id : ''}</dd>
          <dt>Components</dt>
          <dd>
            {insuredPolicyEntity.components
              ? insuredPolicyEntity.components.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {insuredPolicyEntity.components && i === insuredPolicyEntity.components.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
          <dt>Quotation</dt>
          <dd>{insuredPolicyEntity.quotation ? insuredPolicyEntity.quotation.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/insured-policy" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/insured-policy/${insuredPolicyEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default InsuredPolicyDetail;
