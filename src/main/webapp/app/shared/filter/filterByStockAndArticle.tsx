import React, { useState, useEffect } from 'react';
import { translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntities as getStores } from 'app/entities/store/store.reducer';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { Button } from 'reactstrap';
import PropTypes from 'prop-types';

export const FilterByStockAndArticle = ({criteria, setCriteria, onlyArticle}) => {

  const dispatch = useAppDispatch();
  
  const stores = useAppSelector(state => state.store.entities);

  const [searchText, setSearchText] = useState('');
  const [store, setStore] = useState('');


  useEffect(() => {
    dispatch(getStores({}));
  }, []);


  const handleChange = event => {
    setSearchText(event.target.value);
  };

  const handleSelect = event => {
    setStore(event.target.value);
  };

  const pressEnter = event => {
    if (event.keyCode == 13) {
      search();
    }
  };

  const search = () => {
    if(!onlyArticle){
      setCriteria({ ...criteria, storeId: { equals: store }, articleCode: { contains: searchText }, articleDescription: { contains: searchText } });
    }else{
      setCriteria({ ...criteria, articleCode: { contains: searchText }, articleDescription: { contains: searchText } });
    }
  };

  const clear = () => {
    setStore('');
    setSearchText('');
    if(!onlyArticle){
      setCriteria({ ...criteria, storeId: { equals: null }, articleCode: { contains: '' }, articleDescription: { contains: '' } });
    }else{
      setCriteria({ ...criteria, articleCode: { contains: '' }, articleDescription: { contains: '' } });
    }
  };


  let clearButton;
  if (searchText != '' || (store != '' && store != null)) {
    clearButton = <button className="btn btn-danger btn-sm" onClick={clear} onKeyUp={pressEnter}>
      <FontAwesomeIcon icon="trash" />
    </button>;
  } else {
    clearButton = null;
  }


    return(
      <ValidatedForm className="bg-light p-3 rounded" onSubmit={search}>
        {!onlyArticle ? <ValidatedField id="stock-store" name="store" data-cy="store" label={translate('stockmanagerApp.stock.store')} type="select"
          value={store}
          onChange={handleSelect}
        >
          <option value="" key="0">Todos</option>
          {stores
            ? stores.map(otherEntity => (
              <option value={otherEntity.id} key={otherEntity.id}>
                {otherEntity.code}
              </option>
            ))
            : null}
        </ValidatedField>
        : null}
        <ValidatedField
          id="stock-article"
          name="article"
          data-cy="article"
          value={searchText}
          onChange={handleChange}
          label={!onlyArticle ? translate('stockmanagerApp.article.detail.title'):''}
          type="text"
        >
        </ValidatedField>
        <div className="d-flex">
          <Button className="btn btn-info btn-sm" onClick={search}>
            <FontAwesomeIcon icon="search" /> Buscar
          </Button>
          <div>{clearButton}</div>
        </div>
      </ValidatedForm>
    )
};

FilterByStockAndArticle.defaultProps = {
	onlyArticle: false
}

FilterByStockAndArticle.propTypes = {
	onlyArticle: PropTypes.bool
}

export default FilterByStockAndArticle;