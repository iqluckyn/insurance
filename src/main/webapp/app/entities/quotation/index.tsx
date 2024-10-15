import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Quotation from './quotation';
import QuotationDetail from './quotation-detail';
import QuotationUpdate from './quotation-update';
import QuotationDeleteDialog from './quotation-delete-dialog';

const QuotationRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Quotation />} />
    <Route path="new" element={<QuotationUpdate />} />
    <Route path=":id">
      <Route index element={<QuotationDetail />} />
      <Route path="edit" element={<QuotationUpdate />} />
      <Route path="delete" element={<QuotationDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default QuotationRoutes;
