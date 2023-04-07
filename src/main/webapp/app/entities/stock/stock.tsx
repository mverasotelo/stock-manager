import { faCircle } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import FilterByStockAndArticle from 'app/shared/filter/filterByStockAndArticle';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import React, { useEffect, useState } from 'react';
import { getSortState, JhiItemCount, JhiPagination, Translate } from 'react-jhipster';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { getEntities } from './stock.reducer';

export const Stock = () => {
  const dispatch = useAppDispatch();
  const location = useLocation();
  const navigate = useNavigate();  
  const stockList = useAppSelector(state => state.stock.entities);
  const loading = useAppSelector(state => state.stock.loading);
  const totalItems = useAppSelector(state => state.stock.totalItems);

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getSortState(location, ITEMS_PER_PAGE, 'id'), location.search)
  );
  const [criteria, setCriteria] = useState({});


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
    if (location.search !== endURL) {
      navigate(`${location.pathname}${endURL}`);
    }
  };

  // useEffect(() => {
  //   getAllEntities();
  // }, [criteria]);

  useEffect(() => {
    sortEntities();
  }, [paginationState.activePage, paginationState.order, paginationState.sort,criteria]);


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

  return (
    <div>
      <h2 id="stock-heading" data-cy="StockHeading">
        <Translate contentKey="stockmanagerApp.stock.home.title">Stocks</Translate>
        <div className="d-flex justify-content-end">
          {/* <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="stockmanagerApp.stock.home.refreshListLabel">Refresh List</Translate>
          </Button> */}
          <Link to="/stock/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="stockmanagerApp.stock.home.createLabel">Create new Stock</Translate>
          </Link>
        </div>
      </h2>
      <FilterByStockAndArticle setCriteria={setCriteria} criteria={criteria}/>

      <br/>
      <div className="table-responsive">
        {stockList && stockList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                {/* <th className="hand" onClick={sort('id')}>
                    <Translate contentKey="stockmanagerApp.stock.id">Id</Translate> <FontAwesomeIcon icon="sort" />
                  </th> */}
                <th className="hand" onClick={sort('article.code')}>
                  <Translate contentKey="stockmanagerApp.stock.articleCode">Código Producto</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('article.description')}>
                  <Translate contentKey="stockmanagerApp.stock.article">Descripción</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('store.code')}>
                  <Translate contentKey="stockmanagerApp.stock.store">Store</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('actualStock')}>
                  <Translate contentKey="stockmanagerApp.stock.actualStock">Actual Stock</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                {/* <th className="hand" onClick={sort('reorderPoint')}>
                    <Translate contentKey="stockmanagerApp.stock.reorderPoint">Reorder Point</Translate> <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={sort('maxStock')}>
                    <Translate contentKey="stockmanagerApp.stock.maxStock">Max Stock</Translate> <FontAwesomeIcon icon="sort" />
                  </th> */}
                {/* <th className="hand" onClick={sort('section')}>
                    <Translate contentKey="stockmanagerApp.stock.section">Section</Translate> <FontAwesomeIcon icon="sort" />
                  </th> */}
                <th />
              </tr>
            </thead>
            <tbody>
              {stockList.map((stock, i) => (
    
                <tr key={`entity-${i}`} data-cy="entityTable">
                  {/* <td>
                      <Button tag={Link} to={`/stock/${stock.id}`} color="link" size="sm">
                        {stock.id}
                      </Button>
                    </td> */}
                  <td>{stock.article?.code}</td>
                  <td>{stock.article?.description}</td>
                  <td>{stock.store ? stock.store.code : '-'}</td>
                  <td><FontAwesomeIcon icon={faCircle} color={stock.actualStock <= stock.reorderPoint ? "red" : "green"} />{" " + stock.actualStock}</td>
                  {/* <td>{stock.reorderPoint}</td>
                    <td>{stock.maxStock}</td> */}
                  {/* <td>{stock.rack + "-" + stock.level +"-" +stock.section}</td> */}
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/stock/${stock.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/stock/${stock.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/stock/${stock.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
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
              <Translate contentKey="stockmanagerApp.stock.home.notFound">No Stocks found</Translate>
            </div>
          )
        )}
      </div>
      {totalItems ? (
        <div className={stockList && stockList.length > 0 ? '' : 'd-none'}>
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

export default Stock;
