import axios from "axios";
import { isDevMode } from "./common-func.tion";

const baseUrl = isDevMode() ? "/api" : "";
const API = axios.create({
  baseURL: baseUrl,
});

API.interceptors.response.use(
  (value: AxiosResponse<any, any>): AxiosResponse<any, any> => {},
  (err) => {}
);

export { API };
