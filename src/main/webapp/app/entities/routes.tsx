import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Article from './article';
import Stock from './stock';
import Store from './store';
import Action from './action';
import Alert from './alert';
import Employee from './employee';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="article/*" element={<Article />} />
        <Route path="stock/*" element={<Stock />} />
        <Route path="store/*" element={<Store />} />
        <Route path="action/*" element={<Action />} />
        <Route path="alert/*" element={<Alert />} />
        <Route path="employee/*" element={<Employee />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
