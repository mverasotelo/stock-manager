import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IStore } from 'app/shared/model/store.model';
import { StoreType } from 'app/shared/model/enumerations/store-type.model';
import { getEntity, updateEntity, createEntity, reset } from './store.reducer';

export const StoreUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const storeEntity = useAppSelector(state => state.store.entity);
  const loading = useAppSelector(state => state.store.loading);
  const updating = useAppSelector(state => state.store.updating);
  const updateSuccess = useAppSelector(state => state.store.updateSuccess);
  const storeTypeValues = Object.keys(StoreType);

  const handleClose = () => {
    navigate('/store');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...storeEntity,
      ...values,
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
          type: 'MAIN',
          ...storeEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="stockmanagerApp.store.home.createOrEditLabel" data-cy="StoreCreateUpdateHeading">
            <Translate contentKey="stockmanagerApp.store.home.createOrEditLabel">Create or edit a Store</Translate>
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
                  id="store-id"
                  label={translate('stockmanagerApp.store.id')}
                  hidden={true}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('stockmanagerApp.store.code')}
                id="store-code"
                name="code"
                data-cy="code"
                type="text"
                validate={{}}
              />
              <ValidatedField label={translate('stockmanagerApp.store.type')} id="store-type" name="type" data-cy="type" type="select">
                {storeTypeValues.map(storeType => (
                  <option value={storeType} key={storeType}>
                    {translate('stockmanagerApp.StoreType.' + storeType)}
                  </option>
                ))}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/store" replace color="info">
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

export default StoreUpdate;
