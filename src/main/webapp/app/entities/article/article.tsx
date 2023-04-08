import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { Translate, getSortState, JhiPagination, JhiItemCount } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './article.reducer';
import FilterByStockAndArticle from 'app/shared/filter/filterByStockAndArticle';

export const Article = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getSortState(location, ITEMS_PER_PAGE, 'id'), location.search)
  );
  const [searchText, setSearchText] = useState('');
  const [criteria, setCriteria] = useState({});

  const articleList = useAppSelector(state => state.article.entities);
  const loading = useAppSelector(state => state.article.loading);
  const totalItems = useAppSelector(state => state.article.totalItems);

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

  useEffect(() => {
    if (criteria!=null) {
      getAllEntities();
    } 
  }, [criteria, paginationState.activePage]);


  const sortEntities = () => {
    getAllEntities();
    // const endURL = `?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`;
    // if (location.search !== endURL) {
    //   navigate(`${location.pathname}${endURL}`);
    // }
  };

  useEffect(() => {
    sortEntities();
  }, [paginationState.activePage, paginationState.order, paginationState.sort]);

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


  // const handleChange = event => {
  //   setSearchText(event.target.value);
  // };

  // const pressEnter = event => {
  //   if (event.keyCode == 13) {
  //     search();
  //   }
  // };

  // const search = () => {
  //   setCriteria(searchText);
  // };

  // const clear = () => {
  //   setSearchText('');
  //   setCriteria('');
  // };


  // let clearButton;
  // if (searchText != '') {
  //   clearButton = <button className="btn btn-danger btn-sm" onClick={clear}>
  //     <FontAwesomeIcon icon="trash" />
  //   </button>;
  // } else {
  //   clearButton = null;
  // }
  

  return (
    <div>
      <h2 id="action-heading" data-cy="ActionHeading">
        <div className="d-flex justify-content-end">
        <Link to="/article/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="stockmanagerApp.article.home.createLabel">Create new Article</Translate>
          </Link>
        </div>
      </h2>
      <br />
      <Row className="justify-content-center">
        <Col md="8">
          <h2 className="text-center" id="stockmanagerApp.article.home.historyLabel" data-cy="ArticleCreateUpdateHeading">
          <Translate contentKey="stockmanagerApp.article.home.title">Articles</Translate>
          </h2>
        </Col>
      </Row>
      <br />
      <FilterByStockAndArticle setCriteria={setCriteria} criteria={criteria} onlyArticle/>
      {/* <div className="d-flex justify-content-left">
          <label><input type="text" value={searchText} className="form-control form-control-sm" placeholder="Buscar..." onChange={handleChange} onKeyUp={pressEnter}></input></label>
          <Button className="btn btn-info btn-sm" onClick={search}>
            <FontAwesomeIcon icon="search" />
          </Button>
          <div>{clearButton}</div>
        </div> */}
      <br></br><br></br>      <div className="table-responsive">
        {articleList && articleList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                {/* <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="stockmanagerApp.article.id">Id</Translate> <FontAwesomeIcon icon="sort" />
                </th> */}
                <th className="hand" onClick={sort('code')}>
                  <Translate contentKey="stockmanagerApp.article.code">Code</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('description')}>
                  <Translate contentKey="stockmanagerApp.article.description">Description</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('type')}>
                  <Translate contentKey="stockmanagerApp.article.type">Type</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {articleList.map((article, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  {/* <td>
                    <Button tag={Link} to={`/article/${article.id}`} color="link" size="sm">
                      {article.id}
                    </Button>
                  </td> */}
                  <td>{article.code}</td>
                  <td>{article.description}</td>
                  <td>
                    <Translate contentKey={`stockmanagerApp.ArticleType.${article.type}`} />
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      {/* <Button tag={Link} to={`/article/${article.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button> */}
                      <Button
                        tag={Link}
                        to={`/article/${article.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
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
                        to={`/article/${article.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
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
              <Translate contentKey="stockmanagerApp.article.home.notFound">No Articles found</Translate>
            </div>
          )
        )}
      </div>
      {totalItems ? (
        <div className={articleList && articleList.length > 0 ? '' : 'd-none'}>
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

export default Article;
