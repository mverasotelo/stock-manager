import article from 'app/entities/article/article.reducer';
import stock from 'app/entities/stock/stock.reducer';
import store from 'app/entities/store/store.reducer';
import action from 'app/entities/action/action.reducer';
import alert from 'app/entities/alert/alert.reducer';
import employee from 'app/entities/employee/employee.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  article,
  stock,
  store,
  action,
  alert,
  employee,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
