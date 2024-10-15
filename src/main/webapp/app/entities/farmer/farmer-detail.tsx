import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './farmer.reducer';

export const FarmerDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const farmerEntity = useAppSelector(state => state.farmer.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="farmerDetailsHeading">Farmer</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{farmerEntity.id}</dd>
          <dt>
            <span id="firstname">Firstname</span>
          </dt>
          <dd>{farmerEntity.firstname}</dd>
          <dt>
            <span id="lastname">Lastname</span>
          </dt>
          <dd>{farmerEntity.lastname}</dd>
          <dt>
            <span id="email">Email</span>
          </dt>
          <dd>{farmerEntity.email}</dd>
          <dt>
            <span id="position">Position</span>
          </dt>
          <dd>{farmerEntity.position}</dd>
          <dt>
            <span id="phone">Phone</span>
          </dt>
          <dd>{farmerEntity.phone}</dd>
          <dt>
            <span id="address">Address</span>
          </dt>
          <dd>{farmerEntity.address}</dd>
          <dt>
            <span id="city">City</span>
          </dt>
          <dd>{farmerEntity.city}</dd>
          <dt>
            <span id="province">Province</span>
          </dt>
          <dd>{farmerEntity.province}</dd>
          <dt>
            <span id="country">Country</span>
          </dt>
          <dd>{farmerEntity.country}</dd>
          <dt>
            <span id="postalCode">Postal Code</span>
          </dt>
          <dd>{farmerEntity.postalCode}</dd>
          <dt>
            <span id="registrationDate">Registration Date</span>
          </dt>
          <dd>
            {farmerEntity.registrationDate ? (
              <TextFormat value={farmerEntity.registrationDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>User</dt>
          <dd>{farmerEntity.user ? farmerEntity.user.login : ''}</dd>
          <dt>Business</dt>
          <dd>{farmerEntity.business ? farmerEntity.business.registeredName : ''}</dd>
        </dl>
        <Button tag={Link} to="/farmer" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/farmer/${farmerEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default FarmerDetail;
