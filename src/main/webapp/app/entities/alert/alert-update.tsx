import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import React, { useEffect } from 'react';
import { Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';

import { useAppDispatch, useAppSelector } from 'app/config/store';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';

import { getEntities as getSections } from 'app/entities/stock/stock.reducer';
import { AlertType } from 'app/shared/model/enumerations/alert-type.model';
import { createEntity, getEntity, updateEntity } from './alert.reducer';
import Stock from '../stock/stock';

export const AlertUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const stocks = useAppSelector(state => state.stock.entities);
  const alertEntity = useAppSelector(state => state.alert.entity);
  const loading = useAppSelector(state => state.alert.loading);
  const updating = useAppSelector(state => state.alert.updating);
  const updateSuccess = useAppSelector(state => state.alert.updateSuccess);
  const alertTypeValues = Object.keys(AlertType);

  const handleClose = () => {
    navigate('/alert');
  };

  useEffect(() => {
    if (!isNew) {
      dispatch(getEntity(id));
    }

    dispatch(getSections({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    values.datetime = convertDateTimeToServer(values.datetime);
    values.rectificationDatetime = convertDateTimeToServer(values.rectificationDatetime);
    values.stock = {
      id: values.stock,
    };

    const entity = {
      ...alertEntity,
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
      ? {
          datetime: displayDefaultDateTime(),
          rectificationDatetime: displayDefaultDateTime(),
        }
      : {
          type: 'REORDER_POINT',
          ...alertEntity,
          datetime: convertDateTimeFromServer(alertEntity.datetime),
          rectificationDatetime: convertDateTimeFromServer(alertEntity.rectificationDatetime),
          stock: alertEntity?.stock,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="stockmanagerApp.alert.home.createOrEditLabel" data-cy="AlertCreateUpdateHeading">
            <Translate contentKey="stockmanagerApp.alert.home.createOrEditLabel">Create or edit a Alert</Translate>
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
                  id="alert-id"
                  label={translate('stockmanagerApp.alert.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('stockmanagerApp.alert.datetime')}
                id="alert-datetime"
                name="datetime"
                data-cy="datetime"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField label={translate('stockmanagerApp.alert.type')} id="alert-type" name="type" data-cy="type" type="select">
                {alertTypeValues.map(alertType => (
                  <option value={alertType} key={alertType}>
                    {translate('stockmanagerApp.alert.AlertType.' + alertType)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('stockmanagerApp.alert.rectificationDatetime')}
                id="alert-rectificationDatetime"
                name="rectificationDatetime"
                data-cy="rectificationDatetime"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('stockmanagerApp.alert.stock')}
                id="alert-stock"
                data-cy="stock"
                type="select"
                name="stock"  
              >
                <option value="" key="0" />
                {stocks
                  ? stocks.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.store?.code+otherEntity.rack+otherEntity.level+otherEntity.code}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/alert" replace color="info">
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

export default AlertUpdate;
