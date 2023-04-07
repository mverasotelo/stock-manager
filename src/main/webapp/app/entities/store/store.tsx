import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IStore } from 'app/shared/model/store.model';
import { getEntities } from './store.reducer';

export const Store = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();

  const storeList = useAppSelector(state => state.store.entities);
  const loading = useAppSelector(state => state.store.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  return (
    <div>
      <h2 id="store-heading" data-cy="StoreHeading">
        <Translate contentKey="stockmanagerApp.store.home.title">Stores</Translate>
        <div className="d-flex justify-content-end">
          {/* <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="stockmanagerApp.store.home.refreshListLabel">Refresh List</Translate>
          </Button> */}
          <Link to="/store/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="stockmanagerApp.store.home.createLabel">Create new Store</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {storeList && storeList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                {/* <th>
                  <Translate contentKey="stockmanagerApp.store.id">Id</Translate>
                </th> */}
                <th>
                  <Translate contentKey="stockmanagerApp.store.code">Code</Translate>
                </th>
                <th>
                  <Translate contentKey="stockmanagerApp.store.type">Type</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {storeList.map((store, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  {/* <td>
                    <Button tag={Link} to={`/store/${store.id}`} color="link" size="sm">
                      {store.id}
                    </Button>
                  </td> */}
                  <td>{store.code}</td>
                  <td>
                    <Translate contentKey={`stockmanagerApp.StoreType.${store.type}`} />
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      {/* <Button tag={Link} to={`/store/${store.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button> */}
                      <Button tag={Link} to={`/store/${store.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/store/${store.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="stockmanagerApp.store.home.notFound">No Stores found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default Store;
