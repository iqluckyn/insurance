import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Farmer from './farmer';
import FarmerDetail from './farmer-detail';
import FarmerUpdate from './farmer-update';
import FarmerDeleteDialog from './farmer-delete-dialog';

const FarmerRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Farmer />} />
    <Route path="new" element={<FarmerUpdate />} />
    <Route path=":id">
      <Route index element={<FarmerDetail />} />
      <Route path="edit" element={<FarmerUpdate />} />
      <Route path="delete" element={<FarmerDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default FarmerRoutes;
