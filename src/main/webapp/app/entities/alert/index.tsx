import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Alert from './alert';
import AlertDetail from './alert-detail';
import AlertUpdate from './alert-update';
import AlertDeleteDialog from './alert-delete-dialog';

const AlertRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Alert/>} />
    <Route path="new" element={<AlertUpdate />} />
    <Route path=":id">
      <Route index element={<AlertDetail />} />
      <Route path="edit" element={<AlertUpdate />} />
      <Route path="delete" element={<AlertDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default AlertRoutes;
