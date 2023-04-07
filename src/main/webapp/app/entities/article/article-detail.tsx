import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './article.reducer';

export const ArticleDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const articleEntity = useAppSelector(state => state.article.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="articleDetailsHeading">
          <Translate contentKey="stockmanagerApp.article.detail.title">Article</Translate>
        </h2>
        <dl className="jh-entity-details">
          {/* <dt>
            <span id="id">
              <Translate contentKey="stockmanagerApp.article.id">Id</Translate>
            </span>
          </dt>
          <dd>{articleEntity.id}</dd> */}
          <dt>
            <span id="code">
              <Translate contentKey="stockmanagerApp.article.code">Code</Translate>
            </span>
          </dt>
          <dd>{articleEntity.code}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="stockmanagerApp.article.description">Description</Translate>
            </span>
          </dt>
          <dd>{articleEntity.description}</dd>
          <dt>
            <span id="type">
              <Translate contentKey="stockmanagerApp.article.type">Type</Translate>
            </span>
          </dt>
          <dd>{translate('stockmanagerApp.ArticleType.' + articleEntity.type)}</dd>
        </dl>
        <Button onClick={() => history.back()} replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/article/${articleEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ArticleDetail;
