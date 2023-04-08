import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './action.reducer';

export const ActionDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const actionEntity = useAppSelector(state => state.action.entity);
  return (
    <Row>
      <Col md="8">
        <br/>
        <h2 data-cy="actionDetailsHeading">
        <Translate contentKey="stockmanagerApp.action.detail.title" interpolate={{ type: actionEntity.type=="IN" ? "ingreso" : "egreso"}}/>  
        </h2>
        <br/>
        <dl className="jh-entity-details">
          {/* <dt>
            <span id="id">
              <Translate contentKey="stockmanagerApp.action.id">Id</Translate>
            </span>
          </dt> */}
          {/* <dd>{actionEntity.id}</dd> */}
          <dt>
            <span id="code">
              <Translate contentKey="stockmanagerApp.action.code">Code</Translate>
            </span>
          </dt>
          <dd>{actionEntity.code}</dd>
          <dt>
            <span id="datetime">
              <Translate contentKey="stockmanagerApp.action.datetime">Datetime</Translate>
            </span>
          </dt>
          <dd>{actionEntity.datetime ? <TextFormat value={actionEntity.datetime} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="type">
              <Translate contentKey="stockmanagerApp.action.type">Type</Translate>
            </span>
          </dt>
          <dd> {translate('stockmanagerApp.action.actionType.' + actionEntity.type)}</dd>
          <dt>
            <Translate contentKey="stockmanagerApp.store.detail.title">Store</Translate>
          </dt>
          <dd>{actionEntity.stock ? actionEntity.stock.store?.code : ''}</dd>
          <dt>
            <Translate contentKey="stockmanagerApp.action.stock">Article</Translate>
          </dt>
          <dd>{actionEntity.stock ? actionEntity.stock.article?.code + " - " + actionEntity.stock.article?.description : ''}</dd>
          <dt>
            <span id="quantity">
              <Translate contentKey="stockmanagerApp.action.quantity">Quantity</Translate>
            </span>
          </dt>
          <dd>{actionEntity.quantity}</dd>
          {/* <dt>
            <Translate contentKey="stockmanagerApp.action.employee">Employee</Translate>
          </dt> */}
          {/* <dd>{actionEntity.employee ? actionEntity.employee.id : ''}</dd> */}
        </dl>
        <Button tag={Link} to={"/action/type/"+actionEntity.type} replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/action/${actionEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ActionDetail;
