import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import PComponent from './p-component';
import PComponentDetail from './p-component-detail';
import PComponentUpdate from './p-component-update';
import PComponentDeleteDialog from './p-component-delete-dialog';

const PComponentRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<PComponent />} />
    <Route path="new" element={<PComponentUpdate />} />
    <Route path=":id">
      <Route index element={<PComponentDetail />} />
      <Route path="edit" element={<PComponentUpdate />} />
      <Route path="delete" element={<PComponentDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default PComponentRoutes;
