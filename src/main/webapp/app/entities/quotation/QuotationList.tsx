// QuotationList.tsx
import React from 'react';
import { ListGroup, ListGroupItem } from 'reactstrap';

const QuotationList = ({ quotations, onQuotationSelect, selectedQuotation }) => {
  return (
    <div>
      <h3>Quotations</h3>
      <ListGroup>
        {quotations.map(quotation => (
          <ListGroupItem
            key={quotation.id}
            active={selectedQuotation && selectedQuotation.id === quotation.id}
            onClick={() => onQuotationSelect(quotation)}
          >
            Quotation #{quotation.id}
          </ListGroupItem>
        ))}
      </ListGroup>
    </div>
  );
};

export default QuotationList;
