import React, { useEffect, useState } from 'react';
import { yupResolver } from "@hookform/resolvers/yup";
import { useForm } from "react-hook-form";
import axios from 'axios';
import Form from 'react-bootstrap/Form';
import Button from 'react-bootstrap/Button';
import { conversionSchema, ConversionType } from '../schema/conversionSchema';
import { Currency } from '../schema/currencies';
import { getCsrfToken, convertCurrency } from '../rest';

const API_BASE_URL = "http://localhost:8080";

const FrontPage = () => {
  const [csrfToken, setCsrfToken] = useState<string | null>(null);
  const { register, handleSubmit, formState: { errors }, reset } = useForm<ConversionType>({
    resolver: yupResolver(conversionSchema),
  });

  useEffect(() => {
    const fetchCsrfToken = async () => {
      try {
        const response = await getCsrfToken();
        setCsrfToken(response.data.token);
      } catch (error) {
        console.error("Error fetching CSRF token:", error);
      }
    }
    fetchCsrfToken();
  }, []);

  const onSubmitFunc = async (data: ConversionType) => {
    try {
      const response = await convertCurrency(data, csrfToken);

      console.log("Conversion Response:", response.data);
      reset();
    } catch (error) {
      console.error("Error during conversion:", error);
    }
  };

  return (
      <Form onSubmit={handleSubmit(onSubmitFunc)}>
        <h2>Enter currency rates:</h2>
        <br />

        <Form.Group className="mb-3">
          <Form.Label>Source Currency *</Form.Label>
          <Form.Select {...register("sourceCurrency")} className={`form-control ${errors.sourceCurrency ? "is-invalid" : ""}`}>
            <option value="">-- Select source currency --</option>
            {Object.values(Currency).map((currency) => (
              <option key={currency} value={currency}>
                {currency}
              </option>
            ))}
          </Form.Select>
          <Form.Text className="invalid-feedback">
            {errors.sourceCurrency?.message}
          </Form.Text>
        </Form.Group>

        <Form.Group className="mb-3">
          <Form.Label>Target Currency *</Form.Label>
          <Form.Select {...register("targetCurrency")} className={`form-control ${errors.targetCurrency ? "is-invalid" : ""}`}>
            <option value="">-- Select target currency --</option>
            {Object.values(Currency).map((currency) => (
              <option key={currency} value={currency}>
                {currency}
              </option>
            ))}
          </Form.Select>
          <Form.Text className="invalid-feedback">
            {errors.targetCurrency?.message}
          </Form.Text>
        </Form.Group>

        <Form.Group className="mb-3">
          <Form.Label>Amount *</Form.Label>
          <Form.Control {...register("amount")} type="number" step="0.01" className={`form-control ${errors.amount ? "is-invalid" : ""}`} />
          <Form.Text className="invalid-feedback">
            {errors.amount?.message}
          </Form.Text>
        </Form.Group>

        <Button variant="primary" type="submit">
          Submit
        </Button>
      </Form>
  )
}

export default FrontPage;

