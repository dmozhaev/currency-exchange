import axios from 'axios';
import { ConversionType } from './schema/conversionSchema';

const API_BASE_URL = "http://localhost:8080";

// get
export const getCsrfToken = () => axios.get(`${API_BASE_URL}/security/csrf`, {
  withCredentials: true,
})

// post
export const convertCurrency = (data: ConversionType, csrfToken: string) => axios.post(
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
