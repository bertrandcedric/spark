# spark

## Installation environnement de protoypage

### Sous windows : creation d'un docker-machine 
```
docker-machine create --driver virtualbox --virtualbox-memory "4096" dev

fichier de configuration du daemon docker => /var/lib/boot2docker/profile

ajout d'un repertoire partagé dans virtualbox => test
sudo mkdir /jupyter_notebooks
sudo mount -t vboxsf jupyter_notebooks /jupyter_notebooks

sudo mkdir /zeppelin_notebooks
sudo mount -t vboxsf zeppelin_notebooks /zeppelin_notebooks
```

### Image Jupyter
```
docker run -d -p 8888:8888 --name=jupyter -v ${PATH_TO_JUPYTER_NOTEBOOKS_DIRECTORY}:/jupyter_notebooks jupyter/notebook

http://{{ip machine docker}}:8888/tree
```

### Image zeppelin
```
docker run --name zeppelin -d -p 8080:8080 -p 8081:8081 -v ${PATH_TO_ZEPPELIN_NOTEBOOKS_DIRECTORY}:/zeppelin/notebook dylanmei/zeppelin:latest

http://{{ip machine docker}}:8080
```

## MLlib

Algorithms:
=> classification: logistic regression, linear support vector machine (SVM), naive Bayes
=> regression: generalized linear regression (GLM)
=> collaborative filtering: alternating least squares (ALS)
=> clustering: k-means
=> decomposition: singular value decomposition (SVD), principal component analysis (PCA)

## graphic
https://plot.ly/javascript/
