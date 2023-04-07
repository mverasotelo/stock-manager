import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './alert.reducer';

export const AlertDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const alertEntity = useAppSelector(state => state.alert.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="alertDetailsHeading">
          <Translate contentKey="stockmanagerApp.alert.detail.title">Alert</Translate>
        </h2>
        <dl className="jh-entity-details">
          {/* <dt>
            <span id="id">
              <Translate contentKey="stockmanagerApp.alert.id">Id</Translate>
            </span>
          </dt>
          <dd>{alertEntity.id}</dd> */}
          <dt>
            <span id="datetime">
              <Translate contentKey="stockmanagerApp.alert.datetime">Datetime</Translate>
            </span>
          </dt>
          <dd>{alertEntity.datetime ? <TextFormat value={alertEntity.datetime} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="type">
              <Translate contentKey="stockmanagerApp.article.detail.title">Articulo</Translate>
            </span>
          </dt>
          <dd>{alertEntity.stock?.article ? alertEntity.stock?.article?.code +" - "+ alertEntity.stock?.article?.description : "-"}</dd>
          <dt>
            <span id="type">
              <Translate contentKey="stockmanagerApp.alert.type">Type</Translate>
            </span>
          </dt>
          <dd><Translate contentKey={`stockmanagerApp.alert.AlertType.${alertEntity.type}`} /></dd>
          <dt>
            <span id="type">
              <Translate contentKey="stockmanagerApp.store.detail.title">Almacén</Translate>
            </span>
          </dt>
          <dd>{alertEntity.stock?.store ? alertEntity.stock?.store?.code : "-"}</dd>
          <dt>
            <Translate contentKey="stockmanagerApp.alert.section">Section</Translate>
          </dt>
          <dd>
            { alertEntity.stock ? alertEntity.stock?.rack + "-" + alertEntity.stock?.level + "-" + alertEntity.stock?.section : "-"}
          </dd>
          {alertEntity.rectificationDatetime ?
            <>
              <dt>
                <span id="rectificationDatetime">
                  <Translate contentKey="stockmanagerApp.alert.rectificationDatetime">Rectification Datetime</Translate>
                </span>
              </dt>
              <dd>
                <TextFormat value={alertEntity.rectificationDatetime} type="date" format={APP_DATE_FORMAT} />
              </dd>
            </>
            : null}
        </dl>
        <Button tag={Link} to="/alert" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/alert/${alertEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default AlertDetail;
