import React, { useEffect, useState } from 'react';
import { yupResolver } from "@hookform/resolvers/yup";
import { useForm } from "react-hook-form";
import axios from 'axios';
import Form from 'react-bootstrap/Form';
import Button from 'react-bootstrap/Button';
import { conversionSchema, ConversionType } from '../schema/conversionSchema';

const API_BASE_URL = "http://localhost:8080";

const FrontPage = () => {
  const [csrfToken, setCsrfToken] = useState<string | null>(null);
  const { register, handleSubmit, formState: { errors }, reset } = useForm<ConversionType>({
    resolver: yupResolver(conversionSchema),
  });

  useEffect(() => {
    const fetchCsrfToken = async () => {
      try {
        const response = await axios.get(`${API_BASE_URL}/security/csrf`, {
          withCredentials: true,
        });
        setCsrfToken(response.data.token);
      } catch (error) {
        console.error("Error fetching CSRF token:", error);
      }
    }
    fetchCsrfToken();
  }, []);

  const onSubmitFunc = async (data: SignInType) => {
    try {
      const response = await axios.post(
        `${API_BASE_URL}/convert/`,
        data,
        {
          headers: {
            "X-XSRF-TOKEN": csrfToken,
            "Content-Type": "application/json",
          },
          withCredentials: true,
        }
      );

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
          <Form.Control {...register("sourceCurrency")} type="text" className={`form-control ${errors.sourceCurrency ? "is-invalid" : ""}`} />
          <Form.Text className="invalid-feedback">
            {errors.sourceCurrency?.message}
          </Form.Text>
        </Form.Group>

        <Form.Group className="mb-3">
          <Form.Label>Target Currency *</Form.Label>
          <Form.Control {...register("targetCurrency")} type="text" className={`form-control ${errors.targetCurrency ? "is-invalid" : ""}`} />
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

