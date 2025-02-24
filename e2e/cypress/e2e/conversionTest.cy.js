describe('Currency Converter', () => {
  it('should load the page and perform a conversion', () => {
    cy.visit('/');

    // Check if the form exists
    cy.get('[data-testid=container-header]').should('be.visible');
	
	// Enter erroneous data
	cy.get('[data-testid=submit]').should('be.disabled');
	cy.get('[data-testid=sourceCurrency-select]').select('AED');
	cy.get('[data-testid=targetCurrency-select]').select('AED');
	cy.get('[data-testid=targetCurrency-error]').should('be.visible').contains('Source and target currencies must be different');
	cy.get('[data-testid=submit]').should('be.disabled');

	cy.get('[data-testid=amount-input]').type('0');
	cy.get('[data-testid=amount-error]').should('be.visible').contains('Amount must be greater than 0');
	cy.get('[data-testid=submit]').should('be.disabled');

	cy.get('[data-testid=targetCurrency-select]').select('AFN');
	cy.get('[data-testid=targetCurrency-error]').should('not.be.visible');
	cy.get('[data-testid=amount-error]').should('be.visible').contains('Amount must be greater than 0');
	cy.get('[data-testid=submit]').should('be.disabled');

	cy.get('[data-testid=amount-input]').clear().type('1000000.01');
	cy.get('[data-testid=amount-error]').should('be.visible').contains('Amount cannot exceed 1,000,000');
	cy.get('[data-testid=submit]').should('be.disabled');

	cy.get('[data-testid=amount-input]').clear().type('123.456');
	cy.get('[data-testid=amount-error]').should('be.visible').contains('Amount must have at most 2 decimal places');
	cy.get('[data-testid=submit]').should('be.disabled');

	cy.get('[data-testid=amount-input]').clear().type('123.45');
	cy.get('[data-testid=amount-error]').should('not.be.visible');
	cy.get('[data-testid=submit]').should('not.be.disabled');

    // Enter data and submit
	cy.get('[data-testid=sourceCurrency-select]').select('EUR');
	cy.get('[data-testid=targetCurrency-select]').select('USD');
    cy.get('[data-testid=amount-input]').clear().type('100');
    cy.get('[data-testid=submit]').should('not.be.disabled').click();

    // Wait for response and check for conversion result
    cy.get('[data-testid=result-box]').should('be.visible');
    cy.get('[data-testid=result-box-result]').should('not.be.empty');
  });
});
