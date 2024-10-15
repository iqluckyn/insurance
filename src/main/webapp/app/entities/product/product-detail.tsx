import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './product.reducer';

export const ProductDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const productEntity = useAppSelector(state => state.product.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="productDetailsHeading">Product</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{productEntity.id}</dd>
          <dt>
            <span id="productName">Product Name</span>
          </dt>
          <dd>{productEntity.productName}</dd>
          <dt>
            <span id="productCode">Product Code</span>
          </dt>
          <dd>{productEntity.productCode}</dd>
          <dt>
            <span id="productDescription">Product Description</span>
          </dt>
          <dd>{productEntity.productDescription}</dd>
          <dt>
            <span id="isActive">Is Active</span>
          </dt>
          <dd>{productEntity.isActive ? 'true' : 'false'}</dd>
          <dt>
            <span id="createdAt">Created At</span>
          </dt>
          <dd>{productEntity.createdAt ? <TextFormat value={productEntity.createdAt} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="updatedAt">Updated At</span>
          </dt>
          <dd>{productEntity.updatedAt ? <TextFormat value={productEntity.updatedAt} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
        </dl>
        <Button tag={Link} to="/product" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/product/${productEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default ProductDetail;
