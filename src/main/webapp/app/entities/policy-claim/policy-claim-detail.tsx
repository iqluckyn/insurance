import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './policy-claim.reducer';

export const PolicyClaimDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const policyClaimEntity = useAppSelector(state => state.policyClaim.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="policyClaimDetailsHeading">Policy Claim</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{policyClaimEntity.id}</dd>
          <dt>
            <span id="claimNumber">Claim Number</span>
          </dt>
          <dd>{policyClaimEntity.claimNumber}</dd>
          <dt>
            <span id="claimDate">Claim Date</span>
          </dt>
          <dd>
            {policyClaimEntity.claimDate ? <TextFormat value={policyClaimEntity.claimDate} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="amountClaimed">Amount Claimed</span>
          </dt>
          <dd>{policyClaimEntity.amountClaimed}</dd>
          <dt>
            <span id="status">Status</span>
          </dt>
          <dd>{policyClaimEntity.status}</dd>
          <dt>Policy</dt>
          <dd>{policyClaimEntity.policy ? policyClaimEntity.policy.policyNumber : ''}</dd>
        </dl>
        <Button tag={Link} to="/policy-claim" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/policy-claim/${policyClaimEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default PolicyClaimDetail;
