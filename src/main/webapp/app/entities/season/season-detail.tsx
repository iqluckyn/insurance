import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './season.reducer';

export const SeasonDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const seasonEntity = useAppSelector(state => state.season.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="seasonDetailsHeading">Season</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{seasonEntity.id}</dd>
          <dt>
            <span id="seasonName">Season Name</span>
          </dt>
          <dd>{seasonEntity.seasonName}</dd>
          <dt>
            <span id="startDate">Start Date</span>
          </dt>
          <dd>
            {seasonEntity.startDate ? <TextFormat value={seasonEntity.startDate} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="endDate">End Date</span>
          </dt>
          <dd>{seasonEntity.endDate ? <TextFormat value={seasonEntity.endDate} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="isActive">Is Active</span>
          </dt>
          <dd>{seasonEntity.isActive ? 'true' : 'false'}</dd>
          <dt>Crop Type</dt>
          <dd>{seasonEntity.cropType ? seasonEntity.cropType.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/season" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/season/${seasonEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default SeasonDetail;
