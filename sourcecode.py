#!/usr/bin/env python
# coding: utf-8
import sys
from fbprophet import Prophet
import pandas as pd
import matplotlib.pyplot as plt
from matplotlib.backends.backend_pdf import PdfPages

csv_path = sys.argv[1]
periods = sys.argv[2]
pdf_file = sys.argv[3]
investment_amount = sys.argv[4]


gold_ds = pd.read_csv(csv_path) #pd.read_csv("D:\PythonCode\modified.csv")
#gold_ds["ds"] = int(investment_amount) / int(gold_ds["ds"]) * int(gold_ds["ds"])

gold_model = Prophet(interval_width=0.95)
gold_model.fit(gold_ds)
gold_forecast = gold_model.make_future_dataframe(periods=int(periods))
gold_forecast = gold_model.predict(gold_forecast)
another_set=gold_forecast[["ds","yhat_upper"]]
another_set = another_set[another_set.ds >= "2019-04-21"]
today_row = another_set[another_set.ds == "2019-04-21"]
nUnits = float(investment_amount) / float(today_row["yhat_upper"])
calculated_set = another_set["yhat_upper"] * float(nUnits)
calculated_ds = another_set["ds"]
calculated_final_result = pd.DataFrame({"ds":calculated_ds,"y":calculated_set})

pp = PdfPages(pdf_file) 
print(pdf_file)
# PdfPages('multipage.pdf')
plt.figure(figsize=(24, 6))
#gold_model.plot(gold_forecast, xlabel = 'Date', ylabel = 'Gold Price')
plt.plot(calculated_final_result["ds"], calculated_final_result["y"])
plt.title('Gold Pricing')
pp.savefig()
pp.close()

