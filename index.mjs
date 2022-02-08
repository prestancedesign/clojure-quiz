#!/usr/bin/env node

import { loadFile } from 'nbb';
import { fileURLToPath } from 'url';
import { dirname, resolve } from 'path';

const __dirname = fileURLToPath(dirname(import.meta.url));

await loadFile(resolve(__dirname, 'clojurequiz.cljs'));
