import axios from 'axios';
import { TProduct, TTheme, TThemeFolder } from './model.ts';

const BASE_URL = 'http://localhost:8080/';

const createUrlAsString = (path: string, searchParams: any): string => createUrl(path, searchParams).toString();

const createUrl = (path: string, searchParams: any): URL => {
  const url = new URL(path, BASE_URL);
  searchParams && Object.keys(searchParams).forEach((key: string) => {
    const value = searchParams[key as keyof typeof searchParams];
    if (value != null) {
      url.searchParams.set(key, value);
    }
  });
  return url;
};

const fetchProducts = async (themeId?: string): Promise<TProduct[]> => {
  const url = createUrlAsString('/products', { themeId });
  return (await axios.get<TProduct[]>(url)).data ?? [];
};

const fetchThemesFolders = async (): Promise<TThemeFolder[]> => {
  const url = createUrlAsString('/themesfolders', null);
  return (await axios.get<TThemeFolder[]>(url)).data ?? [];
};

const fetchThemes = async (themeFolderId?: string): Promise<TTheme[]> => {
  const url = createUrlAsString('/themes', { themeFolderId });
  return (await axios.get<TTheme[]>(url)).data ?? [];
};

const productUp = async (productId: string): Promise<void> => {
  const url = createUrlAsString('/products/position/up', { productId });
  await axios.patch<void>(url);
};

const productDown = async (productId: string): Promise<void> => {
  const url = createUrlAsString('/products/position/down', { productId });
  await axios.patch<void>(url);
};

const themeUp = async (themeId: string): Promise<void> => {
  const url = createUrlAsString('/themes/position/up', { themeId });
  await axios.patch<void>(url);
};

const themeDown = async (themeId: string): Promise<void> => {
  const url = createUrlAsString('/themes/position/down', { themeId });
  await axios.patch<void>(url);
};

const addProductToThemeFolder = async (productId: string, themeId: string): Promise<void> => {
  const url = createUrlAsString('/themes/products/add', { productId, themeId });
  await axios.patch<void>(url);
};

const addThemeToThemeFolder = async (themeId: string, themeFolderId: string): Promise<void> => {
  const url = createUrlAsString('/themesfolders/themes/add', { themeId, themeFolderId });
  await axios.patch<void>(url);
};

export { fetchProducts, fetchThemesFolders, fetchThemes, addProductToThemeFolder, addThemeToThemeFolder, productUp, productDown, themeUp, themeDown };
