import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import React, { useEffect, useState } from 'react';
import { Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';

import { useAppDispatch, useAppSelector } from 'app/config/store';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';

import { getEntities as getStocks } from 'app/entities/stock/stock.reducer';
import { getEntities as getStores } from 'app/entities/store/store.reducer';
import { ActionType } from 'app/shared/model/enumerations/action-type.model';
import { createEntity, getEntity, updateEntity } from './action.reducer';

export const ActionUpdate = () => {
  const dispatch = useAppDispatch();
  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const { actionType } = useParams();
  const actionEntity = useAppSelector(state => state.action.entity);
  const loading = useAppSelector(state => state.action.loading);
  const updating = useAppSelector(state => state.action.updating);
  const updateSuccess = useAppSelector(state => state.action.updateSuccess);
  const stores = useAppSelector(state => state.store.entities);
  const stocks = useAppSelector(state => state.stock.entities);

  const [selectedStore, setSelectedStore] = useState(0);
  const [selectedStock, setSelectedStock] = useState(0);

  const handleClose = () => {
    navigate('/');
  };

  useEffect(() => {
    if (!isNew) {
      dispatch(getEntity(id));
    }
    // eslint-disable-next-line no-console
    console.log(actionType);
    dispatch(getStores({}));
  }, []);

  useEffect(() => {
    // if (!isNew) {
    //   dispatch(getEntity(id));
    // }
    // // eslint-disable-next-line no-console
    // console.log(actionType);
    dispatch(getStocks({size:100, criteria: {storeId: {equals: selectedStore}}}));
  }, [selectedStore]);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    // values.datetime = convertDateTimeToServer(values.datetime);
    let stock = {id:selectedStock};

    const entity = {
      ...actionEntity,
      ...values,
      type: actionType,
      store:null,
      stock
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };


  // const defaultValues = () =>
  //   isNew
  //     ? {
  //       datetime: displayDefaultDateTime(),
  //     }
  //     : {
  //       type: 'IN',
  //       ...actionEntity,
  //       datetime: convertDateTimeFromServer(actionEntity.datetime),
  //     };

  const selectStore = (event) => {
    setSelectedStore(event.target.value);    
  };

  const selectStock=(event) => {
    setSelectedStock(event.target.value);    
  };



  return (
    <div>
      <br/><br/>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="stockmanagerApp.action.home.createOrEditLabel" data-cy="ActionCreateUpdateHeading">
            {translate('stockmanagerApp.action.home.actionType.' + actionType)}
          </h2>
        </Col>
      </Row>
      <br/>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm onSubmit={saveEntity}>
              {/* {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="action-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('stockmanagerApp.action.datetime')}
                id="action-datetime"
                name="datetime"
                data-cy="datetime"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              /> */}
              {/* <ValidatedField label={translate('stockmanagerApp.action.type')} id="action-type" name="type" data-cy="type" type="select">
                {actionTypeValues.map(actionType => (
                  <option value={actionType} key={actionType}>
                    {translate('stockmanagerApp.ActionType.' + actionType)}
                  </option>
                ))}
              </ValidatedField> */}
              <ValidatedField id="stock-store" name="store" data-cy="store" label={translate('stockmanagerApp.stock.store')} type="select"
                  onChange={selectStore}
                  value={selectedStore}
                  validate={{
                    required: { value: true, message: 'Este campo es obligatorio.' },
                  }}
                  disabled={!isNew}
                >
                  <option value="" key="0">--- Seleccione una opción ---</option>
                  {stores
                    ? stores.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.code}
                      </option>
                    ))
                    : null}
                </ValidatedField>
                <ValidatedField
                  id="action-stock"
                  name="stock"
                  value={selectedStock}
                  onChange={selectStock}
                data-cy="stock"
                disabled={!isNew}
                label={translate('stockmanagerApp.article.detail.title')}
                type="select"
                validate={{
                  required: { value: true, message: 'Este campo es obligatorio.' },
                }}
              >
                <option value="" key="0">--- Seleccione una opción ---</option>
                {stocks
                  ? stocks.map(otherEntity => (
                    <option value={otherEntity.id} key={otherEntity.id}>
                      {otherEntity.article?.code + " - " + otherEntity.article?.description}
                    </option>
                  ))
                    : null}
                </ValidatedField>
                <ValidatedField
                  label={translate('stockmanagerApp.action.quantity')}
                  id="action-quantity"
                  name="quantity"
                  data-cy="quantity"
                  type="number"
                  validate={{
                    required: { value: true, message: 'Este campo es obligatorio.' },
                    min: { value: 1, message: 'La cantidad debe ser mayor a 0.' },
                  }}                />
                { actionType==ActionType.OUT ?
                  <ValidatedField id="action-destination-store" name="destinationStore" data-cy="destination-store" label={translate('stockmanagerApp.action.destinationStore')} type="select"
                  validate={{
                    required: { value: true, message: 'Este campo es obligatorio.' },
                  }}
                  disabled={!isNew}
                >
                <option value="" key="0">--- Seleccione una opción ---</option>
                  {stores 
                    ? stores.filter(otherEntity=> otherEntity.id!=selectedStore).map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.code}
                      </option>
                    ))
                    : null}
                </ValidatedField>
                :null}

                {/* <ValidatedField
                label={translate('stockmanagerApp.action.employee')}
                id="action-employee"
                data-cy="employee"
                type="select"
                name="employees"
              >
                <option value="" key="0" />
                {employees
                  ? employees.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField> */}
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                {translate('stockmanagerApp.action.home.actionType.' + actionType)}
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default ActionUpdate;
