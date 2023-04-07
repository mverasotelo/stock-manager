import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IArticle } from 'app/shared/model/article.model';
import { getEntities as getArticles } from 'app/entities/article/article.reducer';
import { IStore } from 'app/shared/model/store.model';
import { getEntities as getStores } from 'app/entities/store/store.reducer';
import { IStock } from 'app/shared/model/stock.model';
import { getEntity, updateEntity, createEntity, reset } from './stock.reducer';

export const StockUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const articles = useAppSelector(state => state.article.entities);
  const stores = useAppSelector(state => state.store.entities);
  const stockEntity = useAppSelector(state => state.stock.entity);
  const loading = useAppSelector(state => state.stock.loading);
  const updating = useAppSelector(state => state.stock.updating);
  const updateSuccess = useAppSelector(state => state.stock.updateSuccess);

  const handleClose = () => {
    navigate('/stock');
  };

  useEffect(() => {
    if (!isNew) {
      dispatch(getEntity(id));
    }

    dispatch(getArticles({size:2000}));
    dispatch(getStores({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...stockEntity,
      ...values,
      article: articles.find(it => it.id.toString() === values.article.toString()),
      store: stores.find(it => it.id.toString() === values.store.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
        ...stockEntity,
        article: stockEntity?.article?.id,
        store: stockEntity?.store?.id,
      };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="stockmanagerApp.stock.home.createOrEditLabel" data-cy="StockCreateUpdateHeading">
          <Translate contentKey={isNew ? "stockmanagerApp.stock.home.createLabel" : "stockmanagerApp.stock.home.createOrEditLabel" }>Create or edit a Stock</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="stock-id"
                  hidden={true}
                  label={translate('stockmanagerApp.stock.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField id="stock-store" name="store" data-cy="store" label={translate('stockmanagerApp.stock.store')} type="select"
                validate={{
                  required: { value: true, message: 'Este campo es obligatorio.' },
                }}
                disabled={!isNew}
              >
                <option value="" key="0" />
                {stores
                  ? stores.map(otherEntity => (
                    <option value={otherEntity.id} key={otherEntity.id}>
                      {otherEntity.code}
                    </option>
                  ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="stock-article"
                name="article"
                data-cy="article"
                disabled={!isNew}
                label={translate('stockmanagerApp.article.detail.title')}
                type="select"
                validate={{
                  required: { value: true, message: 'Este campo es obligatorio.' },
                }}

              >
                <option value="" key="0" />
                {articles
                  ? articles.map(otherEntity => (
                    <option value={otherEntity.id} key={otherEntity.id}>
                      {otherEntity.code + " - " + otherEntity.description}
                    </option>
                  ))
                  : null}
              </ValidatedField>
              {isNew ? (
                <ValidatedField
                  label={translate('stockmanagerApp.stock.initialStock')}
                  id="stock-actualStock"
                  name="actualStock"
                  data-cy="actualStock"
                  type="number"
                  validate={{
                    required: { value: true, message: 'Este campo es obligatorio.' },
                    min: { value: 0, message: 'El stock debe ser un número positivo.' }
                  }}
                />
              ) : null}

              <ValidatedField
                label={translate('stockmanagerApp.stock.reorderPoint')}
                id="stock-reorderPoint"
                name="reorderPoint"
                data-cy="reorderPoint"
                type="number"
                validate={{
                  required: { value: true, message: 'Este campo es obligatorio.' },
                  min: { value: 0, message: 'El punto de reorden debe ser un número positivo.' }
                }}
              />
              <ValidatedField
                label={translate('stockmanagerApp.stock.maxStock')}
                id="stock-maxStock"
                name="maxStock"
                data-cy="maxStock"
                type="number"
                validate={{
                  required: { value: true, message: 'Este campo es obligatorio.' },
                  min: { value: 1, message: 'La capacidad máxima debe ser mayor a 0.' }
                }}
              />
              <ValidatedField
                label={translate('stockmanagerApp.stock.section')}
                id="stock-section"
                name="section"
                data-cy="section"
                type="text"
                validate={{
                  required: { value: true, message: 'Este campo es obligatorio.' },
                }}
              />
              <ValidatedField label={translate('stockmanagerApp.stock.level')} id="stock-level" name="level" data-cy="level" type="text"
                validate={{
                  required: { value: true, message: 'Este campo es obligatorio.' },
                }}
              />
              <ValidatedField label={translate('stockmanagerApp.stock.rack')} id="stock-rack" name="rack" data-cy="rack" type="text"
                validate={{
                  required: { value: true, message: 'Este campo es obligatorio.' },
                }}
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/stock" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default StockUpdate;
