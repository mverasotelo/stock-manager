import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './store.reducer';

export const StoreDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const storeEntity = useAppSelector(state => state.store.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="storeDetailsHeading">
          <Translate contentKey="stockmanagerApp.store.detail.title">Store</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="stockmanagerApp.store.id">Id</Translate>
            </span>
          </dt>
          <dd>{storeEntity.id}</dd>
          <dt>
            <span id="code">
              <Translate contentKey="stockmanagerApp.store.code">Code</Translate>
            </span>
          </dt>
          <dd>{storeEntity.code}</dd>
          <dt>
            <span id="type">
              <Translate contentKey="stockmanagerApp.store.type">Type</Translate>
            </span>
          </dt>
          <dd>{storeEntity.type}</dd>
        </dl>
        <Button tag={Link} to="/store" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/store/${storeEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default StoreDetail;
