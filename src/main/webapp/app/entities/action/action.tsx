import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import React, { useEffect, useState } from 'react';
import { getSortState, JhiItemCount, JhiPagination, TextFormat, Translate, translate } from 'react-jhipster';
import { Link, useLocation, useNavigate, useParams } from 'react-router-dom';
import { Button, Table } from 'reactstrap';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import FilterByStockAndArticle from 'app/shared/filter/filterByStockAndArticle';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import PropTypes from 'prop-types';
import { getEntities } from './action.reducer';

export const Action = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();
  const { actionType } = useParams();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getSortState(location, ITEMS_PER_PAGE, 'datetime', 'DESC'), location.search)
  );
  const [criteria, setCriteria] = useState({type: {equals: actionType}});

  const actionList = useAppSelector(state => state.action.entities);
  const loading = useAppSelector(state => state.action.loading);
  const totalItems = useAppSelector(state => state.action.totalItems);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        page: paginationState.activePage - 1,
        size: paginationState.itemsPerPage,
        sort: `${paginationState.sort},${paginationState.order}`,
        criteria
      })
    );
  };

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`;
    // if (location.search !== endURL) {
    //   navigate(`${location.pathname}${endURL}`);
    // }
  };

  // useEffect(() => {
  //   // getAllEntities();
  //   sortEntities();
  // }, [criteria]);


  useEffect(() => {
    sortEntities();
  }, [paginationState.activePage, paginationState.order, paginationState.sort, criteria]);

  useEffect(() => {
    const params = new URLSearchParams(location.search);
    const page = params.get('page');
    const sort = params.get(SORT);
    if (page && sort) {
      const sortSplit = sort.split(',');
      setPaginationState({
        ...paginationState,
        activePage: +page,
        sort: sortSplit[0],
        order: sortSplit[1],
      });
    }
  }, [location.search]);

  const sort = p => () => {
    setPaginationState({
      ...paginationState,
      order: paginationState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  const handlePagination = currentPage =>
    setPaginationState({
      ...paginationState,
      activePage: currentPage,
    });

  // const handleSyncList = () => {
  //   sortEntities();
  // };

  return (
    <div>
      <br/><br/>
      <h2 id="action-heading" className="text-center" data-cy="ActionHeading">
        {translate("stockmanagerApp.action.home.history."+actionType)}
        <br/><br/>
        <div className="d-flex justify-content-end">
          {/* <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="stockmanagerApp.action.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/action/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="stockmanagerApp.action.home.createLabel">Create new Action</Translate>
          </Link> */}
        </div>
      </h2>
      <FilterByStockAndArticle criteria={criteria} setCriteria={setCriteria}/>
      <br/>
      <div className="table-responsive">
        {actionList && actionList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                {/* <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="stockmanagerApp.action.id">Id</Translate> <FontAwesomeIcon icon="sort" />
                </th> */}
                <th className="hand" onClick={sort('code')}>
                  <Translate contentKey="stockmanagerApp.action.code">Code</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('datetime')}>
                  <Translate contentKey="stockmanagerApp.action.datetime">Datetime</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                {/* <th className="hand" onClick={sort('type')}>
                  <Translate contentKey="stockmanagerApp.action.type">Type</Translate> <FontAwesomeIcon icon="sort" />
                </th> */}
                <th className="hand" onClick={sort('quantity')}>
                  <Translate contentKey="stockmanagerApp.action.quantity">Quantity</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                {/* <th>
                  <Translate contentKey="stockmanagerApp.action.employee">Employee</Translate> <FontAwesomeIcon icon="sort" />
                </th> */}
                <th className="hand" onClick={sort('stock.article.code')}>
                  <Translate contentKey="stockmanagerApp.action.stock">Stock</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('stock.store.code')}>
                  <Translate contentKey="stockmanagerApp.store.detail.title">Store</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {actionList.map((action, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  {/* <td>
                    <Button tag={Link} to={`/action/${action.id}`} color="link" size="sm">
                      {action.id}
                    </Button>
                  </td> */}
                  <td>{action.code}</td>
                  <td>{action.datetime ? <TextFormat type="date" value={action.datetime} format={APP_DATE_FORMAT} /> : null}</td>
                  {/* <td>
                  {translate('stockmanagerApp.action.actionType.' + action.type)}
                  </td> */}
                  <td>{action.quantity}</td>
                  {/* <td>{action.employee ? <Link to={`/employee/${action.employee.id}`}>{action.employee.id}</Link> : ''}</td> */}
                  <td>{action.stock ? <Link to={`/article/${action.stock?.article?.id}`}>{action.stock?.article?.code}</Link> : ''}</td>
                  <td>{action.store ? action.store.code : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/action/${action.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      {/* <Button
                        tag={Link}
                        to={`/action/${action.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/action/${action.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button> */}
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="stockmanagerApp.action.home.notFound">No Actions found</Translate>
            </div>
          )
        )}
      </div>
      <br/>
      {totalItems ? (
        <div className={actionList && actionList.length > 0 ? '' : 'd-none'}>
          <div className="justify-content-center d-flex">
            <JhiItemCount page={paginationState.activePage} total={totalItems} itemsPerPage={paginationState.itemsPerPage} i18nEnabled />
          </div>
          <div className="justify-content-center d-flex">
            <JhiPagination
              activePage={paginationState.activePage}
              onSelect={handlePagination}
              maxButtons={5}
              itemsPerPage={paginationState.itemsPerPage}
              totalItems={totalItems}
            />
          </div>
        </div>
      ) : (
        ''
      )}
    </div>
  );
};

Action.propTypes= {
  actionType: PropTypes.string.isRequired
}

export default Action;
