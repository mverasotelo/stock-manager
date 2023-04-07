import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './stock.reducer';

export const StockDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const stockEntity = useAppSelector(state => state.stock.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="stockDetailsHeading">
          <Translate contentKey="stockmanagerApp.stock.detail.title">Stock</Translate>
        </h2>
        <dl className="jh-entity-details">
          {/* <dt> */}
          {/* <span id="id">
              <Translate contentKey="stockmanagerApp.stock.id">Id</Translate>
            </span>
          </dt>
          <dd>{stockEntity.id}</dd> */}
          {/* <dt>
            <span id="actualStock">
              <Translate contentKey="stockmanagerApp.stock.actualStock">Actual Stock</Translate>
            </span>
          </dt>
          <dd>{stockEntity.actualStock}</dd> */}
          <dt>
            <Translate contentKey="stockmanagerApp.stock.store">Store</Translate>
          </dt>
          <dd>{stockEntity.store ? stockEntity.store.code : ''}</dd>
          <dt>
            <Translate contentKey="stockmanagerApp.stock.article">Article</Translate>
          </dt>
          <dd>{stockEntity.article ? stockEntity.article.description : ''}</dd>
          <dt>
            <span id="reorderPoint">
              <Translate contentKey="stockmanagerApp.stock.reorderPoint">Reorder Point</Translate>
            </span>
          </dt>
          <dd>{stockEntity.reorderPoint}</dd>
          <dt>
            <span id="maxStock">
              <Translate contentKey="stockmanagerApp.stock.maxStock">Max Stock</Translate>
            </span>
          </dt>
          <dd>{stockEntity.maxStock}</dd>
          <dt>
            <span id="section">
              <Translate contentKey="stockmanagerApp.stock.section">Section</Translate>
            </span>
          </dt>
          <dd>{stockEntity.section}</dd>
          <dt>
            <span id="level">
              <Translate contentKey="stockmanagerApp.stock.level">Level</Translate>
            </span>
          </dt>
          <dd>{stockEntity.level}</dd>
          <dt>
            <span id="rack">
              <Translate contentKey="stockmanagerApp.stock.rack">Rack</Translate>
            </span>
          </dt>
          <dd>{stockEntity.rack}</dd>
        </dl>
        <Button tag={Link} to="/stock" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/stock/${stockEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default StockDetail;
