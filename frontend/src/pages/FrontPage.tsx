import { useEffect, useState } from 'react'
import { yupResolver } from '@hookform/resolvers/yup'
import { useForm } from 'react-hook-form'
import Form from 'react-bootstrap/Form'
import Button from 'react-bootstrap/Button'
import { conversionSchema, ConversionType } from '../schema/conversionSchema'
import { Currency } from '../schema/currencies'
import { getCsrfToken, convertCurrency } from '../rest'
import { toast, Toaster } from 'react-hot-toast'
import './frontpage.css'

const FrontPage = () => {
  const [csrfToken, setCsrfToken] = useState<string | null>(null)
  const [conversionResult, setConversionResult] = useState<ConversionType & { result: number } | null>(null)

  const { register, handleSubmit, formState: { errors, isValid }, reset } = useForm<ConversionType>({
    resolver: yupResolver(conversionSchema),
    mode: 'onChange',
  })

  useEffect(() => {
    const fetchCsrfToken = async () => {
      try {
        const response = await getCsrfToken()
        setCsrfToken(response.data.token)
      } catch (error) {
        console.error('Error fetching CSRF token:', error)
      }
    }
    fetchCsrfToken()
  }, [])

  const onSubmitFunc = async (data: ConversionType) => {
    if (!csrfToken) {
      return
    }
    try {
      const response = await convertCurrency(data, csrfToken)

      setConversionResult({
        sourceCurrency: data.sourceCurrency,
        targetCurrency: data.targetCurrency,
        amount: data.amount,
        result: response.data,
      })

      console.log('Conversion Response:', response.data)
      reset()

      toast.success('Conversion successful! 🎉', { duration: 3000 })
    } catch (error: unknown) {
      console.error('Error during conversion:', error)

      if (error instanceof Error) {
        const axiosError = error as { response?: { data?: string } };
        const errorMessage = axiosError.response?.data ?? error.message;

        toast.error(`Error during conversion, please try again: ❌ ${errorMessage}`, { duration: 10000 });
      } else {
        toast.error('An unknown error occurred during conversion.', { duration: 10000 });
      }
    }
  }

  const formatCurrency = (value: number, currency: string) => {
    return new Intl.NumberFormat(navigator.language, {
      style: 'currency',
      currency: currency,
      minimumFractionDigits: 2,
      maximumFractionDigits: 2,
    }).format(value)
  }

  return (
    <div className="container">
      <Form onSubmit={handleSubmit(onSubmitFunc)}>
        <h2 data-testid="container-header">💵 Currency Converter</h2>
        <br />

        <Form.Group className="form-group">
          <Form.Label>Source Currency *</Form.Label>
          <Form.Select {...register('sourceCurrency')}
            data-testid="sourceCurrency-select"
            className={`form-control ${errors.sourceCurrency ? 'is-invalid' : ''}`}>
            <option value="">-- Select source currency --</option>
            {Object.values(Currency).map((currency) => (
              <option key={currency} value={currency}>
                {currency}
              </option>
            ))}
          </Form.Select>
          <Form.Text className="invalid-feedback" data-testid="sourceCurrency-error">
            {errors.sourceCurrency?.message}
          </Form.Text>
        </Form.Group>

        <Form.Group className="form-group">
          <Form.Label>Target Currency *</Form.Label>
          <Form.Select {...register('targetCurrency')}
            data-testid="targetCurrency-select"
            className={`form-control ${errors.targetCurrency ? 'is-invalid' : ''}`}>
            <option value="">-- Select target currency --</option>
            {Object.values(Currency).map((currency) => (
              <option key={currency} value={currency}>
                {currency}
              </option>
            ))}
          </Form.Select>
          <Form.Text className="invalid-feedback" data-testid="targetCurrency-error">
            {errors.targetCurrency?.message}
          </Form.Text>
        </Form.Group>

        <Form.Group className="form-group">
          <Form.Label>Amount *</Form.Label>
          <Form.Control {...register('amount')}
            data-testid="amount-input"
            type="number"
            step="0.01"
            className={`form-control ${errors.amount ? 'is-invalid' : ''}`} />
          <Form.Text className="invalid-feedback" data-testid="amount-error">
            {errors.amount?.message}
          </Form.Text>
        </Form.Group>

        <Button variant="primary" type="submit" data-testid="submit" disabled={!isValid}>
          Submit
        </Button>
      </Form>

      {conversionResult && (
        <div className="result-box" data-testid="result-box">
          <h4>Conversion Result</h4>
          <p>
            {formatCurrency(conversionResult.amount, conversionResult.sourceCurrency)}
            &nbsp;=&nbsp;
            <strong data-testid="result-box-result">{formatCurrency(conversionResult.result, conversionResult.targetCurrency)}</strong>
          </p>
        </div>
      )}

      <Toaster position="top-center" />
    </div>
  )
}

export default FrontPage
