import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import React, { useEffect, useState } from 'react';
import { getSortState, JhiItemCount, JhiPagination, TextFormat, Translate, translate } from 'react-jhipster';
import { Link, useLocation, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import FilterByStockAndArticle from 'app/shared/filter/filterByStockAndArticle';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import PropTypes from 'prop-types';
import { getEntities } from './alert.reducer';

export const Action = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();
  const { alert } = useParams();
  let isActive = "active" ? true: false;  

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getSortState(location, ITEMS_PER_PAGE, 'datetime', 'DESC'), location.search)
  );
  const [criteria, setCriteria] = useState({isActive});

  const alertList = useAppSelector(state => state.alert.entities);
  const loading = useAppSelector(state => state.alert.loading);
  const totalItems = useAppSelector(state => state.alert.totalItems);

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
    // const endURL = `?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`;
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
      <h2 id="action-heading" data-cy="AlertHeading">
        <div className="d-flex justify-content-end">
          {isActive ?
            <Link to="/history/list" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
                Mostrar alertas activas            
            </Link>
            :
            <Link to="/active/list" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
              <FontAwesomeIcon icon="plus" />
              &nbsp;
              Mostrar alertas activas
            </Link>
          }
        </div>
      </h2>
      <br />
      <Row className="justify-content-center">
        <Col md="8">
          <h2 className="text-center" id="stockmanagerApp.article.home.historyLabel" data-cy="ArticleCreateUpdateHeading">
            {isActive ?
              <Translate contentKey="stockmanagerApp.alert.home.title.active">Alerts</Translate> :
              "Historial de alertas"
            }
          </h2>
        </Col>
      </Row>
      <br />
      <FilterByStockAndArticle setCriteria={setCriteria} criteria={criteria} />
      <br />
      <div className="table-responsive">
        {alertList && alertList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                {/* <th className="hand" onClick={sort('id')}>
                    <Translate contentKey="stockmanagerApp.alert.id">Id</Translate> <FontAwesomeIcon icon="sort" />
                  </th> */}
                <th className="hand" onClick={sort('datetime')}>
                  <Translate contentKey="stockmanagerApp.alert.datetime">Datetime</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('stock.article.code')}>
                  <Translate contentKey="stockmanagerApp.stock.article">Article</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('stock.store.code')}>
                  <Translate contentKey="stockmanagerApp.stock.store">Store</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('type')}>
                  <Translate contentKey="stockmanagerApp.alert.type">Type</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                {!isActive ? (
                  <th className="hand" onClick={sort('rectificationDatetime')}>
                    <Translate contentKey="stockmanagerApp.alert.rectificationDatetime">Rectification Datetime</Translate>{' '}
                    <FontAwesomeIcon icon="sort" />
                  </th>)
                  : null}
                <th />
              </tr>
            </thead>
            <tbody>
              {alertList.map((alert, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  {/* <td>
                      <Button tag={Link} to={`/alert/${alert.id}`} color="link" size="sm">
                        {alert.id}
                      </Button>
                    </td> */}
                  <td>{alert.datetime ? <TextFormat type="date" value={alert.datetime} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>{alert.stock?.article?.code + "-" + alert.stock?.article?.description}</td>
                  <td>{alert.stock?.store?.code}</td>
                  <td>
                    <Translate contentKey={`stockmanagerApp.alert.AlertType.${alert.type}`} />
                  </td>
                  {!isActive ? (
                    <td>
                      <TextFormat type="date" value={alert.rectificationDatetime ? alert.rectificationDatetime : "No subsanada"} format={APP_DATE_FORMAT} />
                    </td>
                  ) : null}
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/alert/${alert.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
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
              <Translate contentKey="stockmanagerApp.alert.home.notFound">No Alerts found</Translate>
            </div>
          )
        )}
      </div>
      {totalItems ? (
        <div className={alertList && alertList.length > 0 ? '' : 'd-none'}>
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

export default Alert;
