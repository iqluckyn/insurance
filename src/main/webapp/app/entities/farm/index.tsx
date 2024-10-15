import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Farm from './farm';
import FarmDetail from './farm-detail';
import FarmUpdate from './farm-update';
import FarmDeleteDialog from './farm-delete-dialog';

const FarmRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Farm />} />
    <Route path="new" element={<FarmUpdate />} />
    <Route path=":id">
      <Route index element={<FarmDetail />} />
      <Route path="edit" element={<FarmUpdate />} />
      <Route path="delete" element={<FarmDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default FarmRoutes;
