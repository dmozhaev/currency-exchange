import * as yup from 'yup'
import { Currency } from './currencies'

export const conversionSchema = yup.object().shape({
  sourceCurrency: yup
    .mixed<Currency>()
    .oneOf(Object.values(Currency), 'Invalid source currency')
    .required('Source currency is required'),

  targetCurrency: yup
    .mixed<Currency>()
    .oneOf(Object.values(Currency), 'Invalid target currency')
    .required('Target currency is required')
    .notOneOf([yup.ref('sourceCurrency')], 'Source and target currencies must be different'),

  amount: yup
    .number()
    .typeError('Amount must be a number')
    .positive('Amount must be greater than 0')
    .max(1000000, 'Amount cannot exceed 1,000,000')
    .test(
      'decimal-places',
      'Amount must have at most 2 decimal places',
      (value) => (value ? /^\d+(\.\d{1,2})?$/.test(value.toString()) : true)
    )
    .required('Amount is required'),
})

export type ConversionType = yup.InferType<typeof conversionSchema>
