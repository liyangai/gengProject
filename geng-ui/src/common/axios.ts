import axios from "axios";
import { isDevMode } from "./common-func.tion";

const baseUrl = isDevMode() ? "/api" : "";
const API = axios.create({
  baseURL: baseUrl,
});
export { API };