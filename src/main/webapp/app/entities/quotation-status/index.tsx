import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import QuotationStatus from './quotation-status';
import QuotationStatusDetail from './quotation-status-detail';
import QuotationStatusUpdate from './quotation-status-update';
import QuotationStatusDeleteDialog from './quotation-status-delete-dialog';

const QuotationStatusRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<QuotationStatus />} />
    <Route path="new" element={<QuotationStatusUpdate />} />
    <Route path=":id">
      <Route index element={<QuotationStatusDetail />} />
      <Route path="edit" element={<QuotationStatusUpdate />} />
      <Route path="delete" element={<QuotationStatusDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default QuotationStatusRoutes;
